# Logregator
A Fabric mod that forwards configured Minecraft and MTR Mod logging Events to a Discord Webhook.
This is designed to be used in The District of Joban, but may be used on other server as well.
Heavily work in progress.

## Config
The config file is located in `<Your MC Folder>/config/logregator/config.json`.  
A sample json can be found below:
```
{
  "events": ["FILTERED_ITEM", "MTR_DATA"],
  "filteredItems": [
    {
      "itemId": "mtr:dashboard",
      "permLevel": [0]
    }
  ],
  "webhook": "<Discord Webhook URL Here>"
}
```

### License
This project is licensed under the MIT License.
