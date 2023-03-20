package com.lx.logregator.data.webhook;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RevoltEmbed implements Embed {
    private String title;
    private String description;
    private String url;
    private Color color;
    private String timestamp;
    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
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

    public Footer getFooter() {
        return footer;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public Image getImage() {
        return image;
    }

    public Author getAuthor() {
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
        this.footer = new Footer(text, icon);
        return this;
    }

    public Embed setThumbnail(String url) {
        this.thumbnail = new Thumbnail(url);
        return this;
    }

    public Embed setImage(String url) {
        this.image = new Image(url);
        return this;
    }

    public Embed setAuthor(String name, String url, String icon) {
        this.author = new Author(name, url, icon);
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

        jsonEmbed.put("type", "Text");
        jsonEmbed.put("title", title);
        jsonEmbed.put("description", getDisplayedDescription());
        jsonEmbed.put("url", url);

        if (color != null) {
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();

            jsonEmbed.put("colour", "rgb(" + r + "," + g + "," + b + ")");
        }

//        Embed.Footer footer = getFooter();
//        Embed.Image image = getImage();
//
//        if (footer != null) {
//            DiscordWebhook.JSONObject jsonFooter = new DiscordWebhook.JSONObject();
//
//            jsonFooter.put("text", footer.getText());
//            jsonFooter.put("icon_url", footer.getIconUrl());
//            jsonEmbed.put("footer", jsonFooter);
//        }
//
//        if (image != null) {
//            DiscordWebhook.JSONObject jsonImage = new DiscordWebhook.JSONObject();
//
//            jsonImage.put("url", image.getUrl());
//            jsonEmbed.put("image", jsonImage);
//        }
        return jsonEmbed;
    }

    private String getDisplayedDescription() {
        StringBuilder sb = new StringBuilder(description);
        for(Embed.Field field : fields) {
            sb.append(String.format("\\n---**%s**---\\n%s", field.getName(), field.getValue()));
        }
        return sb.toString();
    }
}
