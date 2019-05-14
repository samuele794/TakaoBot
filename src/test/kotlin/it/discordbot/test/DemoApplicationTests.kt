package it.discordbot.test

import it.discordbot.database.interfaces.BDONewsInterface
import it.discordbot.database.interfaces.ServerManagementInterface
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private lateinit var managmentServerDiscord: ServerManagementInterface

	@Autowired
	private lateinit var bdoNewsInterface: BDONewsInterface

	@Test
	fun testGetSimbol() {
		assert(managmentServerDiscord.getSimbolCommand("324517087416549377") == "(")
	}

	@Test
	fun testBDONews() {
		bdoNewsInterface.getBDONewsList()
	}

}
