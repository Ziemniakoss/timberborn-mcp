# Timberborn MCP Server

MCP server to control your Timberborn world.
Can read lever and adapter statuses, change lever status and color.
Don't know why you would need that, but it works.
Example MCP configuration:
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