package it.discordbot.test

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.ContentsService
import org.eclipse.egit.github.core.service.RepositoryService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
class DemoApplicationTests {


	@Value("\${git.hub.key}")
	private lateinit var token: String

	@Test
	fun test() {
		val client = GitHubClient()
		client.setOAuth2Token(token)
		val service = RepositoryService()

		val rep = service.getRepository("enrichman", "covid19")

		val contentRepo = ContentsService().getContents(rep, "local/italy")
	}

}
