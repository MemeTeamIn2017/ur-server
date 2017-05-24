package ur.server.test

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ur.server.internal.InternalPacketCodec
import java.util.Random

/**
 * Created by Braynstorm on 7.5.2017 г..
 */
internal class InternalPacketCodecTest {
	@Test
	fun encodeDecodeTest() {
		val rand = Random()
		val num1 = rand.nextFloat()
		val num2 = rand.nextFloat()
		val bool = rand.nextBoolean()
		val str = """\"asdfффaefaegaefaef"""
		
		val mapper = ObjectMapper()
		val startingData = mapper.readTree("""{"a":$num1,               "b": $num2, "c":$bool, "d":"$str"}""")
		
		val encodedBuf = Unpooled.buffer()
		InternalPacketCodec.encode(startingData, encodedBuf)
		val decodedObjList = arrayListOf<Any>()
		InternalPacketCodec.decode(encodedBuf, decodedObjList)
		val decodedJson = decodedObjList[0] as JsonNode
		
		Assertions.assertEquals(startingData.toString(), decodedJson.toString())
		println(decodedJson.toString())
	}
	
	@Test
	fun decodeEncodeTest() {
		val rand = Random()
		val num1 = rand.nextFloat()
		val num2 = rand.nextFloat()
		val bool = rand.nextBoolean()
		val str = """\"asdfaefaegaefaefасдф"""
		
		val startingData = """{"a":$num1,"b":$num2,"c":$bool,"d":"$str"}"""
		val encodedBuf = Unpooled.buffer()
		encodedBuf.writeCharSequence(startingData, Charsets.UTF_8)
		
		val decodedObjList = arrayListOf<Any>()
		InternalPacketCodec.decode(encodedBuf, decodedObjList)
		
		val decodedJson = decodedObjList[0] as JsonNode
		val endBuf = Unpooled.buffer()
		InternalPacketCodec.encode(decodedJson, endBuf)
		endBuf.readBytes(encodedBuf.readerIndex())
		
		Assertions.assertEquals(encodedBuf, endBuf)
		println(decodedJson.toString())
	}
}
