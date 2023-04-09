package com.lx.logregator.data.webhook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    public JsonObject toJson() {
        JsonObject jsonEmbed = new JsonObject();

        jsonEmbed.addProperty("title", title);
        jsonEmbed.addProperty("description", description);
        jsonEmbed.addProperty("timestamp", timestamp);
        jsonEmbed.addProperty("url", url);

        if(timestamp != null) jsonEmbed.addProperty("timestamp", timestamp);

        if (color != null) {
            int rgb = color.getRed();
            rgb = (rgb << 8) + color.getGreen();
            rgb = (rgb << 8) + color.getBlue();

            jsonEmbed.addProperty("color", rgb);
        }

        Embed.Footer footer = getFooter();
        Embed.Image image = getImage();
        Embed.Thumbnail thumbnail = getThumbnail();
        Embed.Author author = getAuthor();
        List<Embed.Field> fields = getFields();

        if (footer != null) {
            JsonObject jsonFooter = new JsonObject();

            jsonFooter.addProperty("text", footer.getText());
            jsonFooter.addProperty("icon_url", footer.getIconUrl());
            jsonEmbed.add("footer", jsonFooter);
        }

        if (image != null) {
            JsonObject jsonImage = new JsonObject();

            jsonImage.addProperty("url", image.getUrl());
            jsonEmbed.add("image", jsonImage);
        }

        if (thumbnail != null) {
            JsonObject jsonThumbnail = new JsonObject();

            jsonThumbnail.addProperty("url", thumbnail.getUrl());
            jsonEmbed.add("thumbnail", jsonThumbnail);
        }

        if (author != null) {
            JsonObject jsonAuthor = new JsonObject();

            jsonAuthor.addProperty("name", author.getName());
            jsonAuthor.addProperty("url", author.getUrl());
            jsonAuthor.addProperty("icon_url", author.getIconUrl());
            jsonEmbed.add("author", jsonAuthor);
        }

        JsonArray jsonArray = new JsonArray();
        for (Embed.Field field : fields) {
            JsonObject jsonField = new JsonObject();

            jsonField.addProperty("name", field.getName());
            jsonField.addProperty("value", field.getValue());
            jsonField.addProperty("inline", field.isInline());

            jsonArray.add(jsonField);
        }

        jsonEmbed.add("fields", jsonArray);
        return jsonEmbed;
    }
}
