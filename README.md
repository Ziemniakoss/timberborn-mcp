# Timberborn MCP Server

Let LLM change and read Timberborn API-exposed elements like levers and switches.

[Model Context Protocol](https://modelcontextprotocol.io/) server that communicates with Timberborn world using HTTP.

## What you can do with it

| Tool                            | What it does                                                                     |
|---------------------------------|----------------------------------------------------------------------------------|
| `timberborn-list-levers`        | List every lever in the world, its on/off state, and whether it's spring-return. |
| `timberborn-change-lever-state` | Turn a named lever on or off.                                                    |
| `timberborn-change-lever-color` | Recolor a named lever (HTML hex, e.g. `ff00ee`).                                 |
| `timberborn-get-lever-status`   | Look up the current state of a single lever.                                     |
| `timberborn-list-adapters`      | List every power adapter in the world and whether it's powered on.               |
| `timberborn-get-base-url`       | Check which URL the server is currently using to reach the game.                 |
| `timberborn-set-api-port`       | Point the server at a different port, if you changed it in the mod's settings.   |

## Setup

```json
{
	"servers": {
		"timberborn-mcp-server": {
			"type": "stdio",
			"command": "/usr/bin/java",
			"args": [
				"-jar",
				"path/to/jar"
			]
		}
	},
	"inputs": []
}
```
