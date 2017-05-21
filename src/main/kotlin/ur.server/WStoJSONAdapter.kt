import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class WStoJSONAdapter : SimpleChannelInboundHandler<TextWebSocketFrame>() {
	override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
		println(msg.text())
	}
	
	fun onNameSet(ctx: ChannelHandlerContext, name: String) {
	
	}
	
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		// on Connect
	}
	
	override fun channelInactive(ctx: ChannelHandlerContext) {
		// on Disconnect
	}
	
}