package it.discordbot.beans.atm

/**
 * Oggetto che rappresenta una linea della metro atm.
 * @property nomeLineMetro String nome della linea (esempio "M1")
 * @property metroDirezioneStatusList ArrayList<MetroDirezioneStatus> la direzione della linea (esempio "Gessate") e lo stato
 * @constructor
 */
data class LineaMetroATM(val nomeLineMetro:String, val metroDirezioneStatusList: ArrayList<MetroDirezioneStatus>)