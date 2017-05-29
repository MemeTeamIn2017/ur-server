package ur.server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ur.server.geoloc.GeoIP

/**
 * Created by braynstorm on 27.05.17.
 */
internal class GeoIPTest {
	@Test
	fun lookupTest() {
		val geo = GeoIP.lookup("127.0.0.1")
		println(geo)
		assertEquals(geo, "US")
	}
}
