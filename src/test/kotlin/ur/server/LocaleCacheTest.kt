package ur.server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by braynstorm on 29/05/17.
 */
internal class LocaleCacheTest {
	
	@Test
	fun testCache() {
		LocaleCache.addCache("127.0.0.1", "us")
		assertEquals("us", LocaleCache.getCached("127.0.0.1"))
	}
	
}
