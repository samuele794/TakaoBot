package it.discordbot.database.repository


interface RSSLinkRepository {

	var lastBDONewsLink: String?

	var lastBDOPatchLink: String?

	var lastATMNewsLink: String?
}