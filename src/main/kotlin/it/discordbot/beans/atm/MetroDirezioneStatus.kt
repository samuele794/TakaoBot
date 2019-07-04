package it.discordbot.beans.atm

/**
 * Oggetto che rappresenta la direzione della linea e il suo stato attuale
 * @property nomeDirezione String nome della direzione della linea (esempio "Gessate")
 * @property stato String stato della linea (esempio "Regolare")
 * @constructor
 */
data class MetroDirezioneStatus(val nomeDirezione: String, var stato:String)