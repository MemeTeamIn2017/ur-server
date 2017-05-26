package ur.server.geoloc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.InetAddress

/**
 * Created by braynstorm on 27.05.17.
 */
internal class GeoIPTest {
	@Test
	fun lookupTest() {
		val geo = GeoIP.lookup(InetAddress.getByAddress(byteArrayOf(127, 0, 0, 1)))
		println(geo)
		assertEquals(geo, "US")
	}
}
