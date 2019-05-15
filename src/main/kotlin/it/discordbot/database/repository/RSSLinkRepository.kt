package it.discordbot.database.repository

import it.discordbot.beans.RSSLink
import org.springframework.data.repository.CrudRepository

interface RSSLinkRepository : CrudRepository<RSSLink, Int> {

	fun getFirstById(id: Long = 1): RSSLink
}