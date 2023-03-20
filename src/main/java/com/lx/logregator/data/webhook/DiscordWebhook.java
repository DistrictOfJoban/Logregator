package com.lx.logregator.data.webhook;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.EventType;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

/**
 * Class used to execute Discord Webhooks with low effort
 * Come from: https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 */
public class DiscordWebhook implements Webhook {

    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private String timestamp;
    private boolean tts;
    private List<Embed> embeds;

    /**
     * Constructs a new DiscordWebhook instance
     *
     * @param url The webhook URL obtained in Discord
     */
    public DiscordWebhook(String url) {
        this.url = url;
        this.embeds = new ArrayList<>();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(Embed embed) {
        this.embeds.add(embed);
    }

    public void send(EventType eventType) throws IOException {
        if(!LogregatorConfig.subscribedEvent.contains(eventType)) return;
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();
            for (Embed embed : this.embeds) {
                JSONObject jsonEmbed = embed.toJson();
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        String requestURL = this.url;

        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(requestURL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                OutputStream stream = connection.getOutputStream();
                stream.write(json.toString().getBytes());
                stream.flush();
                stream.close();

                connection.getInputStream().close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

    }
}