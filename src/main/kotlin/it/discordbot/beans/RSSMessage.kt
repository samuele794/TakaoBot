package it.discordbot.beans

import org.jsoup.nodes.Document

/**
 * Classe che contiene le informazioni utili dei messaggi RSS
 * @property title String Titolo del messaggio
 * @property link String Link del messaggio per il titolo
 * @property doc [Document](https://jsoup.org/apidocs/org/jsoup/nodes/Document.html) Contenuto del messaggio
 * @constructor
 */
data class RSSMessage(val title: String, val link: String, val doc: Document)