package ur.server


import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import mu.KLoggable

class PacketClassToStringEncoder : MessageToMessageEncoder<PacketClass>(), KLoggable {
	override fun encode(ctx: ChannelHandlerContext, msg: PacketClass, out: MutableList<Any>) {
		out.add(JsonUtils stringify msg)
		logger.debug { "Encoded" }
	}
	
	override val logger = logger()
	
	
}

