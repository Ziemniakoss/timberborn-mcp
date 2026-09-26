package eu.ziemniakoss.timberbornmcp.adapters

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

class AdapterToolsTest {

    private lateinit var server: MockRestServiceServer
    private lateinit var adapterTools: AdapterTools

    @BeforeEach
    fun setUp() {
        val builder = RestClient.builder()
        server = MockRestServiceServer.bindTo(builder).build()
        val boberApiService = BoberApiService()
        adapterTools = AdapterTools(boberApiService, builder.build())
    }

    @Test
    fun `lists adapters from api`() {
        server.expect(requestTo("http://localhost:8080/api/adapters/"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(
                    """[{"name":"adapter1","state":true},{"name":"adapter2","state":false}]""",
                    MediaType.APPLICATION_JSON
                )
            )

        val adapters = adapterTools.listAdapters()

        assertEquals(
            listOf(Adapter("adapter1", true), Adapter("adapter2", false)),
            adapters
        )
        server.verify()
    }

    @Test
    fun `uses configured port when listing adapters`() {
        val boberApiService = BoberApiService()
        boberApiService.setBaseUrlForBobers(9090)
        val builder = RestClient.builder()
        server = MockRestServiceServer.bindTo(builder).build()
        adapterTools = AdapterTools(boberApiService, builder.build())

        server.expect(requestTo("http://localhost:9090/api/adapters/"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON))

        val adapters = adapterTools.listAdapters()

        assertEquals(emptyList(), adapters)
        server.verify()
    }
}
