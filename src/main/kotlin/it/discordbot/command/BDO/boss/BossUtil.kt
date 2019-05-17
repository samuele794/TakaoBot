package it.discordbot.command.BDO.boss

import it.discordbot.beans.boss.Boss
import it.discordbot.beans.boss.Giorno
import it.discordbot.core.JDAController
import it.discordbot.database.filter.BDOBossInterface
import net.dv8tion.jda.core.MessageBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Classe per processare i boss e ricavare il boss da notificare
 * @property bdoBossInterface BDOBossInterface
 */
@Scope("singleton")
@Component
class BossUtil {

	@Autowired
	lateinit var bdoBossInterface: BDOBossInterface

	/**
	 * Metodo per ottenere il [Giorno]
	 * @param giorno String nome della giornata corrente in inglese e maiuscolo
	 * @param list ArrayList<Giorno> lista dei [Giorno]
	 * @return Giorno? [Giorno] attuale
	 */
	fun getDayBosses(giorno: String, list: ArrayList<Giorno>): Giorno? {
		val g = giorno.toUpperCase()

		for (bossListDay in list) {
			if (bossListDay.giorno == g) {
				return bossListDay
			}
		}
		return null
	}

	/**
	 * Metodo per ottenere [Boss]
	 * @param ora Int ora per ricavare il boss
	 * @param minuto Int minuti per ricavare il boss
	 * @param list ArrayList<Boss>? lista dei [Boss] di giornata
	 * @return Boss? [Boss] prossimo allo spawn
	 */
	private fun getHourBoss(ora: Int, minuto: Int, list: ArrayList<Boss>?): Boss? {
		val time = StringBuilder().apply {
			append(ora.toString())
			append(":")
			append(minuto.toString())
		}.toString()

		if (list != null) {
			for (boss in list) {
				if (boss.ora == time) {
					return boss
				}
			}
		}
		return null

	}

	/**
	 * Metodo per iniziare la pubblicazione del boss
	 * @param ora Int ora attuale
	 * @param minuto Int minuti attuali
	 * @param giorno Giorno? [Giorno] attuale
	 */
	fun publishBoss(ora: Int, minuto: Int, giorno: Giorno?) {

		when (ora) {
			1, 2, 4, 5, 8, 9, 11, 12, 15, 16, 18, 19 -> processMinute4500(ora, minuto, giorno)
			0, 22, 23 -> processMinute0015(ora, minuto, giorno)
			else -> return
		}
	}

	/**
	 * Metodo per schedulare i boss che spawnano alle 00 dell'ora successiva
	 * @param ora Int ora attuale
	 * @param minuto Int minuti attuali
	 * @param giorno Giorno? [Giorno] attuale
	 */
	private fun processMinute4500(ora: Int, minuto: Int, giorno: Giorno?) {
		if (giorno?.giorno.equals(DayOfWeek.WEDNESDAY.name) && (ora == 11 || ora == 12)) return

		when (minuto) {
			45 -> getHourBoss(ora + 1, 0, giorno?.bosses).apply {
				publish(this?.nomeBoss, "15")
			}

			50 -> getHourBoss(ora + 1, 0, giorno?.bosses).apply {
				publish(this?.nomeBoss, "10")
			}

			55 -> getHourBoss(ora + 1, 0, giorno?.bosses).apply {
				publish(this?.nomeBoss, "5")
			}

			0 -> getHourBoss(ora + 1, 0, giorno?.bosses).apply {
				publish(this?.nomeBoss, "0")
			}
		}
	}

	/**
	 * Metodo per schedulare i boss che spawnano alle 15 dell'ora
	 * @param ora Int ora attuale
	 * @param minuto Int minuti attuali
	 * @param giorno Giorno? [Giorno] attuale
	 */
	private fun processMinute0015(ora: Int, minuto: Int, giorno: Giorno?) {
		if (giorno?.giorno.equals(DayOfWeek.SATURDAY.name) && ora == 22) return

		when (minuto) {
			0 -> getHourBoss(ora, 15, giorno?.bosses).apply {
				publish(this?.nomeBoss, "15")
			}

			5 -> getHourBoss(ora, 15, giorno?.bosses).apply {
				publish(this?.nomeBoss, "10")
			}

			10 -> getHourBoss(ora, 15, giorno?.bosses).apply {
				publish(this?.nomeBoss, "5")
			}

			15 -> getHourBoss(ora, 15, giorno?.bosses).apply {
				publish(this?.nomeBoss, "0")
			}
		}
	}

	/**
	 * Metodo per la pubblicazione effettiva del messaggio
	 * @param bosses Array<String>? lista nomi dei boss
	 * @param orarioMancante String tempo mancante allo spawn del boss
	 */
	private fun publish(bosses: Array<String>?, orarioMancante: String) {
		if (bosses != null) {
			val listServerChannel = bdoBossInterface.getBDOBossChannels()

			val oraAttuale = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

			val message = MessageBuilder().apply {
				append(oraAttuale)
				append(" -> ")
				for (boss in bosses) {
					append(boss)
					append(" ")
				}
				if (orarioMancante == "0") {
					append("sta spawnando")
				} else {
					append("in arrivo tra: ")
					append(orarioMancante)
					append(" minuti")
				}
			}.build()

			for (serverChannel in listServerChannel) {
				JDAController.jda.getGuildById(serverChannel.serverID)
						.getTextChannelById(serverChannel.channelID)
						.sendMessage(message).queue()
			}

		}
	}
}