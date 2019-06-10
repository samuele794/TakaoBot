package it.discordbot.core

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import it.discordbot.command.BDO.BDOCommand
import it.discordbot.command.generalCommand.GeneralCommand
import it.discordbot.command.generalCommand.JoinListener
import it.discordbot.command.image.ImagesCommand
import it.discordbot.command.music.MusicCommand
import it.discordbot.command.tpl.atm.ATMCommand
import it.discordbot.test.TestCommand
import it.discordbot.test.TestWaiter
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.Permission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ExitCodeGenerator
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
class JDAController : ExitCodeGenerator {

	@Value("\${discordBot.jdaToken}")
	private lateinit var jdaToken: String

	companion object {
		lateinit var jda: JDA
		val eventWaiter = EventWaiter()
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

	@Autowired
	lateinit var testWaiter: TestWaiter

	@Autowired
	lateinit var imagesCommand: ImagesCommand


	@PostConstruct
	fun init() {
		jda = JDABuilder(jdaToken).build().apply {
			addEventListener(eventWaiter)
			addEventListener(joinListener)
//			addEventListener(testCommand)
			addEventListener(generalCommand)
			addEventListener(bdoCommand)
			addEventListener(atmCommand)
			addEventListener(musicCommand)
//			addEventListener(testWaiter)
			addEventListener(imagesCommand)
		}
		TakaoLog.logInfo("BOT AVVIATO")
		TakaoLog.logInfo(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR))
	}

	override fun getExitCode(): Int {
		TakaoLog.logInfo("SPEGNIMENTO BOT")
		jda.shutdown()
		return -1
	}

}