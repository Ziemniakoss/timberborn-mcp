package eu.ziemniakoss.timberbornmcp.connection

import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.stereotype.Service

@Service
class BoberApiService {
    var port = 8080

    @McpTool(
        name = "timberborn-set-api-port",
        description = "Change the TCP port used to reach the Timberborn Bobers Api mod running inside the game. " +
            "Timberborn listens on http://localhost:8080 by default, so only call this if adapter or lever tools " +
            "are failing to connect, or if the player has told you they changed the mod's port in its settings."
    )
    fun setBaseUrlForBobers(
        @McpToolParam(description = "TCP port the Bobers Api mod is listening on inside the running Timberborn game, e.g. 8080")
        portOnLocalhost: Int
    ) {
        port = portOnLocalhost
    }

    @McpTool(
        name = "timberborn-get-base-url",
        description = "Return the base URL currently used to reach the Timberborn Bobers Api mod, e.g. http://localhost:8080. " +
            "Useful for diagnosing connection problems before calling other Timberborn tools."
    )
    fun getBobersBaseUrl() = "http://localhost:$port"
}
