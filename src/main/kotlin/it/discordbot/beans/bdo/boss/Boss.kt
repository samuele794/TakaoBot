package it.discordbot.beans.bdo.boss

/**
 * Classe contenitore dei boss in una determinata ora
 * @property ora String ora di spawn del boss
 * @property nomeBoss Array<String> nome o nomi dei boss
 * @constructor
 */
data class Boss(val ora: String, val nomeBoss: Array<String>)