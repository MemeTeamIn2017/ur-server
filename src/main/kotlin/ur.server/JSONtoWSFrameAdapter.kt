package ur.server

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import mu.KLoggable

class JSONtoWSFrameAdapter : MessageToMessageEncoder<JsonNode>(), KLoggable {
	override fun encode(ctx: ChannelHandlerContext, msg: JsonNode, out: MutableList<Any>) {
		out.add(TextWebSocketFrame(JsonUtils stringify msg))
		logger.debug { "Encoded" }
	}
	
	override val logger = logger()
	
	
}

