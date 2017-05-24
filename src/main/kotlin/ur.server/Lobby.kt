package ur.server

import io.netty.channel.Channel
import mu.KLoggable
import mu.KLogger

/**
 * Contains the Lobby logic.
 * Created by Braynstorm on 7.5.2017 г..
 */
object Lobby : KLoggable {
	
	private object Const {
		val NAME_TAKEN = JsonUtils parse """{"id":"auth_status","status":false,"reason":"name_taken"}\n"""
		val AUTH_SUCCESSFUL = JsonUtils parse """{"id":"auth_status","status":true}\n"""
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
	 */
	fun tryAuthenticate(channel: Channel, connectionType: ConnectionType, name: String) {
		if (this has channel) { // the player has already been authenticated.
			logger.info { "Attempted ReAuthentication by ${this[channel]}" }
			return
		}
		
		// Sanitization
		if (name.contains(regexMatchHtmlTag)) {
			// TODO punish(illegal characters in name)
			logger.warn { "Name contains < or > [socket=${channel.remoteAddress()}, name=$name]" }
			return
		}
		
		
		if (this has name) {
			logger.info { "Channel sent a name that is already in use. [socket=${channel.remoteAddress()}, name=$name]" }
			channel.writeAndFlush(Const.NAME_TAKEN)
			return
		}
		
		channels.remove(channel)
		val player = Player(channel, connectionType, name /*, TODO locale */)
		
		playersByName[name] = player
		playersByChannel[channel] = player
		
		logger.info { "Successful authentication. Welcome $player!" }
		player.send(Const.AUTH_SUCCESSFUL)
		// TODO sendUpdateToEveryone.
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



