package it.discordbot.beans.bdo.boss

/**
 * Classe contenitore dei boss del giorno
 * @property giorno String nome del giorno espesso in inglese e in maiuscolo
 * @property bosses ArrayList<Boss> lista dei boss del giorno
 * @constructor
 */
data class Giorno(val giorno: String, val bosses: ArrayList<Boss>)