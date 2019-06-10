package it.discordbot.beans

/**
 * Classe contenitore degli id dei server e relativi canali per
 * le pubblicazioni schedulate
 * @property serverID String id del server
 * @property channelID String id del canale
 * @constructor
 */
data class ServerToChannel(val serverID: String, val channelID: String) {
	override fun equals(other: Any?): Boolean {
		return if (other != null) {
			if (other is ServerToChannel) {
				other.serverID == this.serverID
			} else {
				false
			}
		} else {
			false
		}

	}

	override fun hashCode(): Int {
		var result = serverID.hashCode()
		result = 31 * result + channelID.hashCode()
		return result
	}
}