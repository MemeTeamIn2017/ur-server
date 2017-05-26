package ur.server.geoloc

import java.net.InetAddress

/**
 * API for the internal GEOIP support.
 *
 * Created by braynstorm on 26.05.17.
 */

interface GeoIPProvider {
	
	/**
	 *
	 */
	infix fun lookup(ip: InetAddress): String
}

object GeoIP : GeoIPProvider {
	override fun lookup(ip: InetAddress): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
}
