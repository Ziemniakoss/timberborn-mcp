package eu.ziemniakoss.timberbornmcp.levers

import eu.ziemniakoss.timberbornmcp.BoberApiService
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody

@Component
class LeverTools(val restClient: RestClient, val boberApiService: BoberApiService) {

    @McpTool(name = "timberborn-list-levers", description = "List all levers in timberborn ")
    fun listLevers() = restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/levers")
        .retrieve()
        .requiredBody(object : ParameterizedTypeReference<List<Lever>>() {})

    @McpTool(name = "timberborn-change-lever-state", description = "Turn on or off lever")
    fun changeLeverState(leverName: String, shouldBeTurnedOn: Boolean): String {
        val endpoint = if(shouldBeTurnedOn) "/api/switch-on/" else "/api/switch-off/"
        val fullUri = boberApiService.getBobersBaseUrl() + endpoint + leverName.trim()
     return restClient.get().uri(fullUri).retrieve().requiredBody()
    }

    @McpTool(name = "timberborn-change-lever-color", description = "Use this tool to change lever tool, colors need to be provided in format ff00ee")
    fun changeLeverColor(leverName: String, @McpToolParam(description = "Color to set lever to, use html notation without #, for example ff00ee, 0000ee") color: String): Lever {
        val fullUri = boberApiService.getBobersBaseUrl() + "/api/color/"+ leverName.trim() + "/" + color.trim()
        restClient.get().uri(fullUri).retrieve().body(String::class.java)
        return  getLeverStatus(leverName)
    }

    @McpTool(name = "timberborn-get-lever-status", description = "Get lever status")
    fun getLeverStatus(leverName: String) =restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/levers/${leverName.trim()}")
        .retrieve()
        .requiredBody<Lever>()
}