/**
 * Copyright MemeTeam - 2017
 */
package ur.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.util.concurrent.DefaultEventExecutorGroup
import mu.KotlinLogging
import java.util.*

fun main(args: Array<String>) {
	val logger = KotlinLogging.logger("ur.server.main()")
	
	val port = 4269
	val serverGroup = NioEventLoopGroup(1)
	val clientGroup = NioEventLoopGroup()
	val executorGroup = DefaultEventExecutorGroup(8)
	val bs = ServerBootstrap()
			.group(serverGroup, clientGroup)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<NioSocketChannel>() {
				override fun initChannel(ch: NioSocketChannel) {
					ch.pipeline().addLast(
							HttpRequestDecoder(),
							HttpObjectAggregator(65536),
							HttpResponseEncoder(),
							WebSocketServerProtocolHandler("/"),
							JSONtoWSFrameAdapter()
					).addLast(executorGroup, StringToJSONAdapter()
//					).addLast(executorGroup, JSONtoWSFrameAdapter()
					)
					/*, JsonPacketCodec(), PacketReceiveHandler(), PacketSendHandler(), FallbackReadHandler()*/
				}
			})
	
	val future = bs.bind(port).addListener { future ->
		with(future as ChannelFuture) {
			if (future.isSuccess) {
				logger.info { "Server open on $port" }
				future.channel().closeFuture().addListener {
					serverGroup.shutdownGracefully()
					clientGroup.shutdownGracefully()
				}
			} else {
				logger.error(future.cause()) { "Server failed to open on $port" }
				serverGroup.shutdownGracefully()
				clientGroup.shutdownGracefully()
			}
		}
	}
	
	
	val sc = Scanner(System.`in`)
	
	while (sc.nextLine() != "exit") {
	}
	
	sc.close()
	future.channel().close()
	
	
}

