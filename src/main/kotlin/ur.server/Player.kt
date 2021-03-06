package ur.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.Channel
import mu.KLoggable
import mu.KLogger

@JsonIgnoreProperties("connectionType", "remoteAddress", "logger")
data class Player(private val channel: Channel,
                  val connectionType: ConnectionType,
                  val name: String,
                  val locale: String,
                  var ingame: Boolean = false
) : KLoggable {
	
	override val logger: KLogger = logger()
	
	val remoteAddress: String = channel.remoteAddress().toString()
	
	/**
	 * Sends [packet] over the underlying [channel].
	 */
	fun send(packet: JsonNode) {
		channel.writeAndFlush(JsonUtils stringify packet)
		logger.trace { "Sent JsonNode to player" }
	}
	
	fun send(string: String) {
		channel.writeAndFlush(string)
		logger.trace { "Sent raw string to player" }
	}
	
	fun send(packet: PacketClass) {
		channel.writeAndFlush(packet)
		logger.trace { "Sent PacketClass to player." }
	}
	
	/**
	 * Called when a [packet] is received from this player's [Channel].
	 * It already known that the packet's `id` field is textual.
	 */
	fun receive(packetID: Packet, packet: JsonNode) {
		logger.trace { "Packet received from Player. $this" }
		when (packetID) {
			Packet.CHALLENGE_PLAYER -> {
				// TODO addFunctionality.
				logger.trace { "Got CHALLENGER_PLAYER packet. Handling (WIP)." }
			}
			else -> {
				// We got a packet that is either AUTH or is a Server Packet.
				// both situations are undesirable. We punish for both.
				
				// TODO punish(illegal packet received from player).
				logger.warn { "Got Server packet from client [packetID=$packetID, player=$this]" }
			}
		}
		
	}
	
}
