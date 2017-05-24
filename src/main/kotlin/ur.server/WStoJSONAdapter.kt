import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import mu.KLoggable
import mu.KLogger
import ur.server.ConnectionType
import ur.server.JsonUtils
import ur.server.Lobby


class WStoJSONAdapter : SimpleChannelInboundHandler<TextWebSocketFrame>(), KLoggable {
	override val logger: KLogger = logger()
	
	override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
		val channel = ctx.channel()
		val json: JsonNode
		
		try {
			json = JsonUtils parse msg.text()
		} catch(e: JsonParseException) {
			// TODO punish(connection sent invalid json)
			logger.warn { "Caught invalid JSON from client. ${channel.remoteAddress()}" }
			return
		}
		
		
		val packetID = json["id"].let {
			if (!it.isTextual) {
				// TODO punish(invalid packet)
				return
			}
			
			it.asText()
		}
		
		
		
		
		if (Lobby.has(channel)) {
			Lobby[channel].receive(json)
		} else {
			if (packetID != "auth") {
				// TODO punish(non-player sent non-auth packet)
				logger.warn { "Non-player sent non-auth packet. [socket=${channel.remoteAddress()}, id=\"${json.get("id")}\"" }
				return
			}
			
			// TODO sanitize name
			
			Lobby.tryAuthenticate(channel, ConnectionType.WEB_SOCKET, json["name"].asText())
		}
	}
	
	private fun onNameSet(ctx: ChannelHandlerContext, name: String) {
		// called when the auth packet is received
	}
	
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		logger.info { "Connected ${ctx.channel().remoteAddress()} }" }
	}
	
	// on Connect
	override fun channelInactive(ctx: ChannelHandlerContext) {
		// on Disconnect
		logger.info { "Disconnected ${ctx.channel().remoteAddress()} }" }
	}
}
