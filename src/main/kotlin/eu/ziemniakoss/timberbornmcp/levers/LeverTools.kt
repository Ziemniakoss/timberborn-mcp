package eu.ziemniakoss.timberbornmcp.levers

import eu.ziemniakoss.timberbornmcp.connection.BoberApiService
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody

@Component
class LeverTools(val restClient: RestClient, val boberApiService: BoberApiService) {

    @McpTool(
        name = "timberborn-list-levers",
        description = "List every lever placed in the current Timberborn world, including each lever's name, " +
            "whether it is currently on or off, and whether it is a spring-return (momentary) lever that resets " +
            "itself after being toggled. Use this to discover what levers exist before switching or recoloring one."
    )
    fun listLevers() = restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/levers")
        .retrieve()
        .requiredBody(object : ParameterizedTypeReference<List<Lever>>() {})

    @McpTool(
        name = "timberborn-change-lever-state",
        description = "Switch a specific lever in the Timberborn world on or off, for example to open a sluice, " +
            "operate a mechanism, or toggle anything wired to that lever. The lever must already exist in the world " +
            "(use timberborn-list-levers to find valid names). The in-game state change happens asynchronously in " +
            "the background, so trust this tool's returned status as confirmation of success and do not immediately " +
            "call timberborn-get-lever-status or timberborn-list-levers afterwards expecting to see the new state — " +
            "the game may not have applied it yet."
    )
    fun changeLeverState(
        @McpToolParam(description = "Exact name of the lever to switch, as returned by timberborn-list-levers")
        leverName: String,
        @McpToolParam(description = "true to turn the lever on, false to turn it off")
        shouldBeTurnedOn: Boolean
    ): String {
        val endpoint = if(shouldBeTurnedOn) "/api/switch-on/" else "/api/switch-off/"
        val fullUri = boberApiService.getBobersBaseUrl() + endpoint + leverName.trim()
     return restClient.get().uri(fullUri).retrieve().requiredBody()
    }

    @McpTool(
        name = "timberborn-change-lever-color",
        description = "Recolor a specific lever in the Timberborn world, for example to color-code levers by what " +
            "they control. Returns the lever's updated status after the color change."
    )
    fun changeLeverColor(
        @McpToolParam(description = "Exact name of the lever to recolor, as returned by timberborn-list-levers")
        leverName: String,
        @McpToolParam(description = "Color to set the lever to, in HTML hex notation without the leading #, for example ff00ee or 0000ee")
        color: String
    ): Lever {
        val fullUri = boberApiService.getBobersBaseUrl() + "/api/color/"+ leverName.trim() + "/" + color.trim()
        restClient.get().uri(fullUri).retrieve().body(String::class.java)
        return  getLeverStatus(leverName)
    }

    @McpTool(
        name = "timberborn-get-lever-status",
        description = "Get the current status (on/off state and spring-return flag) of a single named lever " +
            "in the Timberborn world. Use this to check a lever's state before or after toggling it."
    )
    fun getLeverStatus(
        @McpToolParam(description = "Exact name of the lever to look up, as returned by timberborn-list-levers")
        leverName: String
    ) = restClient
        .get()
        .uri(boberApiService.getBobersBaseUrl() + "/api/levers/${leverName.trim()}")
        .retrieve()
        .requiredBody<Lever>()
}