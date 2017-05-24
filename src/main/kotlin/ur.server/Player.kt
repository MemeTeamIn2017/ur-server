package ur.server

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.Channel
import mu.KLoggable
import mu.KLogger
import java.util.*

data class Player(private val channel: Channel,
                  val connectionType: ConnectionType,
                  val name: String,
                  val locale: Locale = Locale.ENGLISH,
                  var ingame: Boolean = false
) : KLoggable {
	override val logger: KLogger = logger()
	
	val remoteAddress: String = channel.remoteAddress().toString()
	
	/**
	 * Sends [packet] over the underlying [channel].
	 */
	fun send(packet: JsonNode) {
		channel.writeAndFlush(packet)
	}
	
	/**
	 * Called when a [packet] is received from this player's [Channel].
	 * It already known that the packet's `id` field is textual.
	 */
	fun receive(packet: JsonNode) {
		logger.info { "Packet received. $this" }
	}
	
	
}
