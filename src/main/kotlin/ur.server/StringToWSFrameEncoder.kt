package ur.server

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import mu.KLoggable
import mu.KLogger

/**
 * Created by braynstorm on 24.05.17.
 */
class StringToWSFrameEncoder : MessageToMessageEncoder<String>(), KLoggable {
	override val logger: KLogger = logger()
	
	override fun encode(ctx: ChannelHandlerContext, msg: String, out: MutableList<Any>) {
		out.add(TextWebSocketFrame(msg))
		logger.debug { "Encoded" }
	}
	
}
