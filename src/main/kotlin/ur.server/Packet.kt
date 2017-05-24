package ur.server

import com.fasterxml.jackson.databind.JsonNode

/**
 * Created by braynstorm on 24.05.17.
 */
enum class Packet {
	AUTH,
	AUTH_STATUS,
	CHALLENGE_PLAYER,
	CHALLENGE_RESPONSE;
	
	/**
	 * @return true only if the packet id matches this packet.
	 */
	fun isThisPacket(json: JsonNode) = json["id"].asText() == this.name
}
