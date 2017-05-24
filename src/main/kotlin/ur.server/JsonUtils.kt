package ur.server

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStream

/**
 * Created by braynstorm on 24.05.17.
 */
object JsonUtils {
	private val mapper = ObjectMapper()
	infix fun parse(string: String): JsonNode = mapper.readTree(string)
	infix fun parse(stream: InputStream): JsonNode = mapper.readTree(stream)
	infix fun stringify(msg: Any): String = mapper.writeValueAsString(msg)
	
}
