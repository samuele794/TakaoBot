package it.discordbot.beans

import org.jsoup.nodes.Document

data class RSSMessage(val title: String, val link: String, val doc: Document)