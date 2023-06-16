# Logregator
A Fabric mod that forwards configured Minecraft and optionally MTR Mod logging Events to a Discord Webhook.
This is designed to be used in The District of Joban, but may be used on other server as well.
Heavily work in progress.

## Config
The config file is located in `<Your MC Folder>/config/logregator/config.json`.  
A sample json can be found below:
```
{
  "webhook": "<Discord Webhook URL Here>",
  "events": {
    "filteredItems": [
      {
        "itemId": "mtr:dashboard",
        "permLevel": [0, 1]
      }
    ],
    "blockBreak": [
      {
        "blockId": "minecraft:grass_block",
        "area": {
          "pos1": [5, 0, 5],
          "pos2": [-5, 255, -5]
        },
        "permLevel": [0, 1]
      }
    ],
    "blockPlace": [
      {
        "blockId": "minecraft:grass_block",
        "area": {
          "pos1": [5, 0, 5],
          "pos2": [-5, 255, -5]
        },
        "permLevel": [0, 1]
      }
    ],
    "mtr": {
      "logBlock": true,
      "logRailwayData": true
    }
  }
}
```

### License
This project is licensed under the MIT License.
