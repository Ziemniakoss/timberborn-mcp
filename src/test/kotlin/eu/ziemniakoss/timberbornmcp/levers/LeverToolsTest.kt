package eu.ziemniakoss.timberbornmcp.levers

import eu.ziemniakoss.timberbornmcp.connection.BoberApiService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals

class LeverToolsTest {

    private lateinit var server: MockRestServiceServer
    private lateinit var leverTools: LeverTools

    @BeforeEach
    fun setUp() {
        val builder = RestClient.builder()
        server = MockRestServiceServer.bindTo(builder).build()
        val boberApiService = BoberApiService()
        leverTools = LeverTools(builder.build(), boberApiService)
    }

    @Test
    fun `lists levers from api`() {
        server.expect(requestTo("http://localhost:8080/api/levers"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    """[{"name":"lever1","state":true,"springReturn":false}]""",
                    MediaType.APPLICATION_JSON
                )
            )

        val levers = leverTools.listLevers()

        assertEquals(listOf(Lever("lever1", state = true, springReturn = false)), levers)
        server.verify()
    }

    @Test
    fun `switches lever on`() {
        server.expect(requestTo("http://localhost:8080/api/switch-on/lever1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("ok", MediaType.TEXT_PLAIN))

        val result = leverTools.changeLeverState("lever1", true)

        assertEquals("ok", result)
        server.verify()
    }

    @Test
    fun `switches lever off and trims name`() {
        server.expect(requestTo("http://localhost:8080/api/switch-off/lever1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("ok", MediaType.TEXT_PLAIN))

        val result = leverTools.changeLeverState("  lever1  ", false)

        assertEquals("ok", result)
        server.verify()
    }

    @Test
    fun `changes lever color then fetches updated status`() {
        server.expect(requestTo("http://localhost:8080/api/color/lever1/ff00ee"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess())
        server.expect(requestTo("http://localhost:8080/api/levers/lever1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    """{"name":"lever1","state":true,"springReturn":false}""",
                    MediaType.APPLICATION_JSON
                )
            )

        val lever = leverTools.changeLeverColor("lever1", "ff00ee")

        assertEquals(Lever("lever1", true, springReturn = false), lever)
        server.verify()
    }

    @Test
    fun `gets lever status and trims name`() {
        server.expect(requestTo("http://localhost:8080/api/levers/lever1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    """{"name":"lever1","state":false,"springReturn":true}""",
                    MediaType.APPLICATION_JSON
                )
            )

        val lever = leverTools.getLeverStatus("  lever1  ")

        assertEquals(Lever("lever1", false, springReturn = true), lever)
        server.verify()
    }
}
