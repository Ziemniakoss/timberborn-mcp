package eu.ziemniakoss.timberbornmcp.connection

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoberApiServiceTest {

    @Test
    fun `defaults to port 8080`() {
        val service = BoberApiService()

        assertEquals("http://localhost:8080", service.getBobersBaseUrl())
    }

    @Test
    fun `changing port updates base url`() {
        val service = BoberApiService()

        service.setBaseUrlForBobers(9999)

        assertEquals("http://localhost:9999", service.getBobersBaseUrl())
    }
}
