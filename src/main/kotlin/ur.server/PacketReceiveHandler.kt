package ur.server

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.handler.codec.ByteToMessageCodec
import ur.server.internal.InternalPacketCodec

/**
 * TODO Add class description
 * Created by Braynstorm on 7.5.2017 г..
 */
val jsonObjectMapper = ObjectMapper().also {
	//	it.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false)
}
//val logger = KotlinLogging.logger { }

class JsonPacketCodec : ByteToMessageCodec<JsonNode>(false) {
//	override val logger: KLogger = KotlinLogging.logger("JsonPacketCodec")
	
	override fun decode(ctx: ChannelHandlerContext, data: ByteBuf, out: MutableList<Any>) {
		try {
			InternalPacketCodec.decode(data, out)
		} catch (e: JsonParseException) {
//			logger.debug { e }
			ctx.fireChannelRead(data)
		}
	}
	
	override fun encode(ctx: ChannelHandlerContext, msg: JsonNode, out: ByteBuf) {
		InternalPacketCodec.encode(msg, out)
	}
	
}

class PacketReceiveHandler : SimpleChannelInboundHandler<JsonNode>() {
	override fun channelActive(ctx: ChannelHandlerContext) {
		println("[Connect][${String.format("", System.currentTimeMillis())}] ${ctx.channel().remoteAddress()}")
		super.channelActive(ctx)
	}
	
	override fun channelInactive(ctx: ChannelHandlerContext) {
		println("[Disconnect][${String.format("", System.currentTimeMillis())}] ${ctx.channel().remoteAddress()}")
		super.channelInactive(ctx)
	}
	
	override fun channelRead0(ctx: ChannelHandlerContext, msg: JsonNode) {
		println(msg.toString())
	}
}


class PacketSendHandler : ChannelOutboundHandlerAdapter() {
	override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
		if (msg is ByteBuf) {
			ctx.writeAndFlush(msg)
			promise.setSuccess()
		} else {
			promise.setFailure(ClassCastException("${msg.javaClass.simpleName} is not an instance of ${ByteBuf::class.java.simpleName}"))
		}
	}
}

class FallbackReadHandler : ChannelInboundHandlerAdapter() {
	override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
		println("fail")
		ctx.close()
	}
	
	override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
		println(cause)
		println("punishing")
		ctx?.close()
		
	}
}
