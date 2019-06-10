package it.discordbot.beans.image

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Scope("singleton")
@Component
class ImageCommandProxy {

	@Autowired
	private lateinit var context: ApplicationContext

	fun getLoremPicsumMachine(): LoremPicsumStateMachine {
		return context.getBean("loremPicsumStateMachine") as LoremPicsumStateMachine

	}
}