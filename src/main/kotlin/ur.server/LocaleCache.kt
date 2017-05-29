package ur.server

import mu.KLoggable
import org.sqlite.SQLiteException
import java.sql.DriverManager

object LocaleCache : KLoggable, AutoCloseable {
	override val logger = logger()
	
	private val connection = DriverManager.getConnection("jdbc:sqlite:./cache.db")
	
	private val stmtFindLocale = connection.prepareStatement("SELECT locale FROM locales WHERE ip=?")
	private val stmtAddIpLocalePair = connection.prepareStatement("INSERT INTO locales (ip, locale) VALUES(?,?)")
	
	init {
		connection.autoCommit = true
	}
	
	fun getCached(ip: String): String? {
		synchronized(connection) {
			stmtFindLocale.setString(1, ip)
			val result = stmtFindLocale.executeQuery()
			stmtFindLocale.clearParameters()
			if (!result.isAfterLast) {
				logger.trace { "Found cache for $ip" }
				return result.getString(1)
			} else {
				logger.trace { "Didn't find cache for $ip" }
				return null
			}
		}
	}
	
	fun addCache(ip: String, locale: String) {
		synchronized(connection) {
			stmtAddIpLocalePair.setString(1, ip)
			stmtAddIpLocalePair.setString(2, locale)
			try {
				stmtAddIpLocalePair.execute()
				logger.trace { "Successfully added cache entry: [ip=$ip, locale=$locale]" }
			} catch (e: SQLiteException) {
				// This should be impossible, because we will first check for that
				// and then we will add it only if it isn't found.
				logger.warn(e) { "IP already has cache" }
			} finally {
				stmtAddIpLocalePair.clearParameters()
			}
		}
	}
	
	override fun close() {
		connection.close()
		stmtFindLocale.close()
		stmtAddIpLocalePair.close()
	}
	
	
}
