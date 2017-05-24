package ur.server

import io.netty.channel.Channel

/**
 * Created by braynstorm on 24.05.17.
 */

internal class NoSuchPlayerException(name: String) : Exception(name)
internal class NoSuchChannelException(channel: Channel) : Exception(channel.remoteAddress().toString())
