package com.lx.logregator.data.webhook;

import com.google.gson.JsonObject;

import java.awt.*;
import java.util.Date;
import java.util.List;

public interface Embed {
    String getTitle();
    String getDescription();
    String getTimestamp();
    String getUrl();
    Color getColor();
    Footer getFooter();
    Thumbnail getThumbnail();
    Image getImage();
    Author getAuthor();
    List<Field> getFields();
    Embed setTitle(String title);
    Embed setDescription(String description);
    Embed setUrl(String url);
    Embed setColor(Color color);
    Embed setFooter(String text, String icon);
    Embed setThumbnail(String url);
    Embed setImage(String url);
    Embed setAuthor(String name, String url, String icon);
    Embed setTimestamp();
    Embed setTimestamp(Date date);
    Embed addField(String name, String value, boolean inline);
    JsonObject toJson();

    class Footer {
        private String text;
        private String iconUrl;

        public Footer(String text, String iconUrl) {
            this.text = text;
            this.iconUrl = iconUrl;
        }

        public String getText() {
            return text;
        }

        public String getIconUrl() {
            return iconUrl;
        }
    }

    class Thumbnail {
        private String url;

        public Thumbnail(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    class Image {
        private String url;

        public Image(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    class Author {
        private String name;
        private String url;
        private String iconUrl;

        public Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getIconUrl() {
            return iconUrl;
        }
    }

    class Field {
        private String name;
        private String value;
        private boolean inline;

        public Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isInline() {
            return inline;
        }
    }
}
