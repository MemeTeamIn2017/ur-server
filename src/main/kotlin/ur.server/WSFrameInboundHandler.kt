package ur.server

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import mu.KLoggable
import mu.KLogger


class WSFrameInboundHandler : SimpleChannelInboundHandler<TextWebSocketFrame>(), KLoggable {
	override val logger: KLogger = logger()
	
	/**
	 * Gets called when the channel receives a full String packet.
	 */
	override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
		val channel = ctx.channel()
		val json: JsonNode
		
		try {
			json = JsonUtils parse msg.text()
		} catch(e: JsonParseException) {
			// TODO punish(connection sent invalid json)
			if (Lobby has ctx.channel()) {
				logger.info(e) { "Invalid JSON received from Player. ${Lobby[ctx.channel()]}" }
			} else {
				logger.info(e) { "Invalid JSON received from Channel. ${channel.remoteAddress()}" }
			}
			return
		}
		
		
		val packetID = json["id"].let {
			if (!it.isTextual) {
				// TODO punish(invalid packet id)
				if (Lobby has ctx.channel()) {
					logger.info { "Invalid Packet (packet id isn't string) received from Player. ${Lobby[ctx.channel()]}" }
				} else {
					logger.info { "Invalid Packet (packet id isn't string) received from Channel. ${channel.remoteAddress()}" }
				}
				return
			}
			
			val txt = it.asText().toUpperCase()
			
			try {
				// "return"
				Packet.valueOf(txt)
			} catch (e: IllegalArgumentException) {
				// TODO punish(unknown packet id)
				if (Lobby has ctx.channel()) {
					logger.info(e) { "Unknown packet ID received from Player. [id=$txt, player=${Lobby[ctx.channel()]}]" }
				} else {
					logger.info(e) { "Unknown packet ID received from Channel. [id=$txt, channel=${channel.remoteAddress()}]" }
				}
				return
			}
		}
		
		
		if (!Lobby.has(channel)) {
			if (packetID == Packet.AUTH) {
				Lobby.tryAuthenticate(channel, ConnectionType.WEB_SOCKET, json["name"].asText(), json["locale"].asText())
			} else {
				// TODO punish(non-player sent non-auth packet)
				logger.warn { "Non-player sent non-auth packet. [socket=${channel.remoteAddress()}, id=$packetID]" }
			}
		} else {
			if (Lobby has channel) {
				// the player has already been authenticated.
				// TODO punish(player reAuth)
				logger.warn { "Attempted ReAuthentication by ${Lobby[channel]}" }
			} else {
				Lobby[channel].receive(packetID, json)
			}
		}
	}
	
	private fun onNameSet(ctx: ChannelHandlerContext, name: String) {
		// called when the auth packet is received
	}
	
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		logger.info { "Connected ${ctx.channel().remoteAddress()} }" }
		Lobby.addChannel(ctx.channel())
	}
	
	// on Connect
	override fun channelInactive(ctx: ChannelHandlerContext) {
		// on Disconnect
		logger.info { "Disconnected ${ctx.channel().remoteAddress()} }" }
		Lobby.removeChannel(ctx.channel())
		
	}
	
	
}
