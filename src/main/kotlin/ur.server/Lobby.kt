package ur.server

import io.netty.channel.Channel

/**
 * TODO Add class description
 * Created by Braynstorm on 7.5.2017 г..
 */
class Lobby {
	private val players: MutableMap<String, Channel> = hashMapOf()
	
	fun isNameTaken(name: String): Boolean = players.containsKey(name)
	
	
}
