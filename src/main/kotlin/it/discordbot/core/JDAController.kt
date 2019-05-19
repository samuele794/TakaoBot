package it.discordbot.core

import it.discordbot.TestCommand
import it.discordbot.command.BDO.BDOCommand
import it.discordbot.command.generalCommand.GeneralCommand
import it.discordbot.command.generalCommand.JoinListener
import it.discordbot.command.music.MusicCommand
import it.discordbot.command.tpl.atmAlert.ATMCommand
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Classe principale di avviamento del Bot Discord
 * @property jdaToken String Chiave API per il bot Discord
 * @property testCommand TestCommand
 * @property generalCommand GeneralCommand
 * @property bdoCommand BDOCommand
 * @property atmCommand ATMCommand
 * @property musicCommand MusicCommand
 * @property joinListener JoinListener
 */
@Scope(value = "singleton")
@Component
class JDAController {

	@Value("\${discordBot.jdaToken}")
	private lateinit var jdaToken: String

	companion object {
		lateinit var jda: JDA
	}

	@Autowired
	lateinit var testCommand: TestCommand

	@Autowired
	lateinit var generalCommand: GeneralCommand

	@Autowired
	lateinit var bdoCommand: BDOCommand

	@Autowired
	lateinit var atmCommand: ATMCommand

	@Autowired
	lateinit var musicCommand: MusicCommand

	@Autowired
	lateinit var joinListener: JoinListener


	@PostConstruct
	fun init() {
		jda = JDABuilder(jdaToken).build().apply {
			addEventListener(joinListener)
			addEventListener(testCommand)
			addEventListener(generalCommand)
			addEventListener(bdoCommand)
			addEventListener(atmCommand)
			addEventListener(musicCommand)
		}
		TakaoLog.logInfo("BOT AVVIATO")
	}

}