import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class WStoJSONAdapter : SimpleChannelInboundHandler<TextWebSocketFrame>() {
	override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
		println(msg.text())
	}
	
	fun onNameSet(ctx: ChannelHandlerContext, name: String) {
		// called when
	}
	
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		// on Connect
		println("[Connect][${String.format("", System.currentTimeMillis())}] ${ctx.channel().remoteAddress()}")
	}
	
	override fun channelInactive(ctx: ChannelHandlerContext) {
		// on Disconnect
		println("[Disconnect][${String.format("", System.currentTimeMillis())}] ${ctx.channel().remoteAddress()}")
		
	}
	
}
