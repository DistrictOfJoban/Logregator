package com.lx.logregator.data.webhook;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscordEmbed implements Embed {
    private String title;
    private String description;
    private String url;
    private Color color;
    private String timestamp;

    private Embed.Footer footer;
    private Embed.Thumbnail thumbnail;
    private Embed.Image image;
    private Embed.Author author;
    private final java.util.List<Embed.Field> fields = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    public Color getColor() {
        return color;
    }

    public Embed.Footer getFooter() {
        return footer;
    }

    public Embed.Thumbnail getThumbnail() {
        return thumbnail;
    }

    public Embed.Image getImage() {
        return image;
    }

    public Embed.Author getAuthor() {
        return author;
    }

    public List<Embed.Field> getFields() {
        return fields;
    }

    public Embed setTitle(String title) {
        this.title = title;
        return this;
    }

    public Embed setDescription(String description) {
        this.description = description;
        return this;
    }

    public Embed setUrl(String url) {
        this.url = url;
        return this;
    }

    public Embed setColor(Color color) {
        this.color = color;
        return this;
    }

    public Embed setFooter(String text, String icon) {
        this.footer = new Embed.Footer(text, icon);
        return this;
    }

    public Embed setThumbnail(String url) {
        this.thumbnail = new Embed.Thumbnail(url);
        return this;
    }

    public Embed setImage(String url) {
        this.image = new Embed.Image(url);
        return this;
    }

    public Embed setAuthor(String name, String url, String icon) {
        this.author = new Embed.Author(name, url, icon);
        return this;
    }

    public Embed setTimestamp() {
        this.timestamp = Instant.now().toString();
        return this;
    }

    public Embed setTimestamp(Date date) {
        this.timestamp = date.toInstant().toString();
        return this;
    }

    public Embed addField(String name, String value, boolean inline) {
        this.fields.add(new Embed.Field(name, value, inline));
        return this;
    }

    @Override
    public DiscordWebhook.JSONObject toJson() {
        DiscordWebhook.JSONObject jsonEmbed = new DiscordWebhook.JSONObject();

        jsonEmbed.put("title", title);
        jsonEmbed.put("description", description);
        jsonEmbed.put("timestamp", timestamp);
        jsonEmbed.put("url", url);

        if(timestamp != null) jsonEmbed.put("timestamp", timestamp);

        if (color != null) {
            int rgb = color.getRed();
            rgb = (rgb << 8) + color.getGreen();
            rgb = (rgb << 8) + color.getBlue();

            jsonEmbed.put("color", rgb);
        }

        Embed.Footer footer = getFooter();
        Embed.Image image = getImage();
        Embed.Thumbnail thumbnail = getThumbnail();
        Embed.Author author = getAuthor();
        List<Embed.Field> fields = getFields();

        if (footer != null) {
            DiscordWebhook.JSONObject jsonFooter = new DiscordWebhook.JSONObject();

            jsonFooter.put("text", footer.getText());
            jsonFooter.put("icon_url", footer.getIconUrl());
            jsonEmbed.put("footer", jsonFooter);
        }

        if (image != null) {
            DiscordWebhook.JSONObject jsonImage = new DiscordWebhook.JSONObject();

            jsonImage.put("url", image.getUrl());
            jsonEmbed.put("image", jsonImage);
        }

        if (thumbnail != null) {
            DiscordWebhook.JSONObject jsonThumbnail = new DiscordWebhook.JSONObject();

            jsonThumbnail.put("url", thumbnail.getUrl());
            jsonEmbed.put("thumbnail", jsonThumbnail);
        }

        if (author != null) {
            DiscordWebhook.JSONObject jsonAuthor = new DiscordWebhook.JSONObject();

            jsonAuthor.put("name", author.getName());
            jsonAuthor.put("url", author.getUrl());
            jsonAuthor.put("icon_url", author.getIconUrl());
            jsonEmbed.put("author", jsonAuthor);
        }

        List<DiscordWebhook.JSONObject> jsonFields = new ArrayList<>();
        for (Embed.Field field : fields) {
            DiscordWebhook.JSONObject jsonField = new DiscordWebhook.JSONObject();

            jsonField.put("name", field.getName());
            jsonField.put("value", field.getValue());
            jsonField.put("inline", field.isInline());

            jsonFields.add(jsonField);
        }

        jsonEmbed.put("fields", jsonFields.toArray());
        return jsonEmbed;
    }
}
