package ur.server

/**
 * Created by braynstorm on 24.05.17.
 */
interface PacketClass

data class PacketPlayerList(val players: Collection<Player>) : PacketClass {
	val id = Packet.LOBBY_LIST
}

class PlayerJoinedPacket(val player: Player) : PacketClass {
	val id = Packet.PLAYER_JOINED_LOBBY
}
