package eu.ziemniakoss.timberbornmcp.adapters

import eu.ziemniakoss.timberbornmcp.BoberApiService
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class Tools(val boberApiService: BoberApiService, val restClient: RestClient) {
    @McpTool(name = "timberborn-list-adapters", description = "List Adapters defined in timberborn world")
    fun listAdapters() = restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/adapters/")
        .retrieve()
        .requiredBody(object : ParameterizedTypeReference<List<Adapter>>() {})
}