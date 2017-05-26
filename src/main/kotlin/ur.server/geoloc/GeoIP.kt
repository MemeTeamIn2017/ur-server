package ur.server.geoloc

import mu.KLoggable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketAddress
import java.net.URI

/**
 * API for the internal GeoIP support.
 *
 * Created by braynstorm on 26.05.17.
 */

interface GeoIPProvider {
	
	/**
	 * Performs a lookup on this IP address to determine the country code.
	 * If the IP is in the database, it directly reads the country code from there.
	 * Otherwise it performs a GeoIP lookup from a web service.
	 *
	 * This method caches any results that it gets in the lookup table.
	 */
	infix fun lookup(ip: String): String
	
	/**
	 * [SocketAddress] version of [lookup]
	 */
	infix fun lookup(ip: SocketAddress): String {
		val string = ip.toString()
		return lookup(string.substring(0, string.indexOf('/')))
	}
}

object GeoIP : GeoIPProvider, KLoggable {
	override val logger = logger()
	
	override fun lookup(ip: String): String {
		
		// TODO perform lookup in the database
		return getCountry(ip)
	}
	
	
	fun getCountry(ip: String): String {
		val connection = URI.create("http://freegeoip.net/csv/$ip").toURL().openConnection() as HttpURLConnection
		connection.requestMethod = "GET"
		connection.doOutput = true
		connection.useCaches = true
		connection.connect()
		
		// TODO potential optimization by using the stream directly and parsing only until the second comma,
		// discarding the rest.
		val reader = BufferedReader(InputStreamReader(connection.inputStream))
		val line = reader.readLine()
		reader.close()
		
		val firstComma = line.indexOf(',') + 1
		val result = line.substring(
				firstComma,
				firstComma + 2
		)
		
		println(line)
		
		return result
		
	}
}
