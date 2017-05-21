package ur.server

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.Channel
import java.util.*

/**
 * TODO Add class description
 * Created by Braynstorm on 7.5.2017 г..
 */
object Lobby {
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
			// TODO log it
			println("[Lobby] Attempted reAuthentication from ${this[channel].remoteAddress}. Ignoring.")
			return
		}
		
		var name = name
		
		// Sanitization
		
		if (name.contains(regexMatchHtmlTag)) {
			// TODO log it
		}
		
		
		if (this has name) {
			// the name is taken
			// TODO log it
			println("[Lobby]NameTaken")
			return
		}
		
		channels.remove(channel)
		val player = Player(channel, connectionType, name /*, TODO locale */)
		
		playersByName[name] = player
		playersByChannel[channel] = player
		
		
		println("[Lobby] Log in $name:${player.remoteAddress} ")
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
	 * Removes all signs of [channel] in the Lobby.
	 */
	fun removeChannel(channel: Channel) {
		if (has(channel)) {
			playersByName.remove(playersByChannel.remove(channel)?.name ?: return)
		}
		
		channels.remove(channel)
	}
}

internal class NoSuchPlayerException(name: String) : Exception(name)
internal class NoSuchChannelException(channel: Channel) : Exception(channel.remoteAddress().toString())


data class Player(private val channel: Channel,
                  val connectionType: ConnectionType,
                  val name: String,
                  val locale: Locale = Locale.ENGLISH,
                  var ingame: Boolean = false
) {
	
	val remoteAddress: String = channel.remoteAddress().toString()
	
	/**
	 * Sends [packet] over the underlying [channel].
	 */
	fun send(packet: JsonNode) {
		channel.writeAndFlush(packet)
	}
	
	fun receive(packet: JsonNode) {
	
	}
	
	
}

enum class ConnectionType {
	WEB_SOCKET
}
