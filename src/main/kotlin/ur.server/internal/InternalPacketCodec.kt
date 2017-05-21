package ur.server.internal

import com.fasterxml.jackson.databind.JsonNode
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import ur.server.jsonObjectMapper

/**
 * Handles the encoding/decoding of packets from bytes/json to json/bytes.
 * Created by Braynstorm on 7.5.2017 г..
 */
object InternalPacketCodec {
	@JvmStatic fun encode(data: JsonNode, out: ByteBuf) {
		out.writeCharSequence(data.toString(), Charsets.UTF_8)
	}
	
	@JvmStatic fun decode(data: ByteBuf, out: MutableList<Any>) {
		val stream = ByteBufInputStream(data)
		val rootNode: JsonNode = jsonObjectMapper.readTree(stream)
		out.add(rootNode)
	}
	
}
