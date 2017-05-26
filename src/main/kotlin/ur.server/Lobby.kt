package ur.server

import io.netty.channel.Channel
import mu.KLoggable
import mu.KLogger
import java.util.*

/**
 * Contains the Lobby logic.
 * Created by Braynstorm on 7.5.2017 г..
 */
object Lobby : KLoggable {
	
	private object Const {
		val AUTH_FAIL_TAKEN = """{"id":"${Packet.AUTH_STATUS.name}","status":false,"reason":"TAKEN"}"""
		val AUTH_FAIL_LENGTH = """{"id":"${Packet.AUTH_STATUS.name}","status":false,"reason":"LENGTH"}"""
		val AUTH_FAIL_ILLEGAL_CHARACTERS = """{"id":"${Packet.AUTH_STATUS.name}","status":false,"reason":"ILLEGAL_CHARACTERS"}"""
		val AUTH_FAIL_INVALID_LOCALE = """{"id":"${Packet.AUTH_STATUS.name}","status":false,"reason":"INVALID_LOCALE"}"""
		val AUTH_SUCCESSFUL = """{"id":"${Packet.AUTH_STATUS.name}","status":true}"""
		
		val LOCALES = Locale.getISOCountries().toSet()
	}
	
	override val logger: KLogger = logger()
	
	/**
	 * Holds all players, key-ed by name.
	 */
	private val playersByName: MutableMap<String, Player> = hashMapOf()
	/**
	 * Holds all player, key-ed by [Channel].
	 */
	private val playersByChannel: MutableMap<Channel, Player> = hashMapOf()
	/**
	 * Holds all [Channel]s which haven't authenticated yet.
	 */
	private val channels: MutableSet<Channel> = hashSetOf()
	
	operator fun get(name: String): Player {
		return playersByName[name] ?: throw NoSuchPlayerException(name)
	}
	
	operator fun get(channel: Channel): Player {
		return playersByChannel[channel] ?: throw NoSuchChannelException(channel)
	}
	
	/**
	 * Gets called when [channel] sends an AUTHENTICATE packet.
	 * If [locale] is null, then it will use GeoIPProvider to determine the locale.
	 */
	fun tryAuthenticate(channel: Channel, connectionType: ConnectionType, name: String, locale: String) {
		// Data normalization
		
		
		if (this has name) {
			logger.info { "Channel sent a name that is already in use. [socket=${channel.remoteAddress()}, name=$name]" }
			channel.writeAndFlush(Const.AUTH_FAIL_TAKEN)
			return
		}
		
		if (name.length < 4 || name.length > 20) {
			logger.info { "Length error on authentication. [socket=${channel.remoteAddress()}, name=$name]" }
			channel.writeAndFlush(Const.AUTH_FAIL_LENGTH)
			return
		}
		
		if (name.contains(regexMatchHtmlTag)) {
			// TODO punish(illegal characters in name)
			logger.warn { "Name contains < or >. [socket=${channel.remoteAddress()}, name=$name]" }
			channel.writeAndFlush(Const.AUTH_FAIL_ILLEGAL_CHARACTERS)
			return
		}
		
		var needsToKnowLocale = false
		
		val countryCode: String = if (locale != "") {
			logger.trace { "Got a non-empty locale" }
			if (Const.LOCALES.contains(locale)) {
				logger.trace { "Locale is valid." }
				locale
			} else {
				logger.info { "Locale is not valid" }
				return
			}
		} else {
			// TODO addFunctionality - geoIP
			needsToKnowLocale = true
			logger.trace { "Using GeoIPProvider to determine locale." }
			return
		}
		
		// We've passed all the tests. Make the new player and add them to the lobby
		// action! :D
		
		channels.remove(channel)
		
		val player = Player(channel, connectionType, name, countryCode)
		
		val playerListPacket = JsonUtils stringify PacketPlayerList(playersByName.values)
		val playerJoinedPacket = JsonUtils stringify PlayerJoinedPacket(player)
		
		
		playersByChannel.forEach { _, player ->
			player.send(playerJoinedPacket)
		}
		
		playersByName[name] = player
		playersByChannel[channel] = player
		
		logger.info { "Successful authentication. Welcome $player!" }
		player.send(JsonUtils stringify AuthStatusPacket(true, countryCode = countryCode))
		
		logger.debug { playerListPacket }
		player.send(playerListPacket)
		player.send(JsonUtils stringify "{}")
		
	}
	
	/**
	 * @return TRUE if there is a player with that name playing, FALSE otherwise.
	 */
	infix fun has(name: String): Boolean = playersByName.containsKey(name)
	
	/**
	 * @return TRUE if [channel] is authenticated, valid player.
	 */
	infix fun has(channel: Channel): Boolean = playersByChannel.containsKey(channel)
	
	/**
	 * Gets called on channelActive evnet.
	 * Must add the channel to the non-auth set.
	 */
	fun addChannel(channel: Channel) {
		if (has(channel)) {
			logger.warn { "Retention error, player fired ChannelActive event. ${this[channel]}" }
			return
		}
		
		channels += channel
	}
	
	/**
	 * Removes all signs of [channel] in the Lobby.
	 * Silent.
	 */
	fun removeChannel(channel: Channel) {
		logger.trace { "Removing channel ${channel.remoteAddress()}" }
		if (has(channel)) {
			playersByName.remove(playersByChannel.remove(channel)?.name ?: return)
		}
		
		channels -= channel
	}
	
	/**
	 * Indicates the amount of [Player] instances currently online.
	 */
	val connectedPlayers get () = playersByChannel.entries.size
}



