package it.discordbot.beans.image

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Proxy per restituire bean dei comandi per immagini
 * @property context ApplicationContext
 */
@Scope("singleton")
@Component
class ImageCommandProxy {

	@Autowired
	private lateinit var context: ApplicationContext

	/**
	 * Metodo che restituisce la procedura per il LoremPicsum
	 * @return LoremPicsumStateMachine
	 */
	fun getLoremPicsumMachine(): LoremPicsumStateMachine {
		return context.getBean("loremPicsumStateMachine") as LoremPicsumStateMachine

	}
}