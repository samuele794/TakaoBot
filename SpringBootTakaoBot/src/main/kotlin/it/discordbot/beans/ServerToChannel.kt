package it.discordbot.beans


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
}