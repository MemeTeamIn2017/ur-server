package ur.server

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import mu.KLoggable

class JSONtoStringAdapter : ChannelOutboundHandlerAdapter(), KLoggable {
	override val logger = logger()
	override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
		if (msg is JsonNode) {
			val str = JsonUtils stringify msg
			logger.info { str }
			ctx.write(TextWebSocketFrame(str))
		} else {
		}
		super.write(ctx, msg, promise)
	}
}

