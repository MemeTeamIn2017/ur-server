package ur.server

/**
 * Created by braynstorm on 26.05.17.
 */
data class AuthStatusPacket(val status: Boolean, val reason: String? = null, val countryCode: String? = null) : PacketClass {
	val id = Packet.AUTH_STATUS.name
}
