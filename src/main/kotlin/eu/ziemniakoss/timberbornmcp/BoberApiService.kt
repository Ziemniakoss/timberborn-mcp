package eu.ziemniakoss.timberbornmcp

import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class BoberApiService {
    var port = 8080
    @McpTool(name = "timberborn-set-api-port", description = "If your timberborn does not use 8080, use this method to changed used endpoint")
    fun setBaseUrlForBobers(portOnLocalhost: Int) {
        port = portOnLocalhost
    }

    @McpTool(name = "timberborn-get-base-url", description = "Get base url for timberborn API")
    fun getBobersBaseUrl() = "http://localhost:$port"

    @Bean
    fun getBoberRestClient() = RestClient.builder().build()
}