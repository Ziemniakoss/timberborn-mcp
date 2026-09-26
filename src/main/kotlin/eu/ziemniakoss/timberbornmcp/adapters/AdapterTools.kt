package eu.ziemniakoss.timberbornmcp.adapters

import eu.ziemniakoss.timberbornmcp.connection.BoberApiService
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class AdapterTools(val boberApiService: BoberApiService, val restClient: RestClient) {
    @McpTool(
        name = "timberborn-list-adapters",
        description = "List every power adapter placed in the current Timberborn world, along with whether each one " +
            "is currently powered on or off. Adapters connect mechanical and electrical power networks together. " +
            "Use this tool when the player asks about power/adapter status or wants an overview of their power network."
    )
    fun listAdapters() = restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/adapters/")
        .retrieve()
        .requiredBody(object : ParameterizedTypeReference<List<Adapter>>() {})
}