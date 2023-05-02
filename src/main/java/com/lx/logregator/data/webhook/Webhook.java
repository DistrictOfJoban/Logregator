package com.lx.logregator.data.webhook;


import java.io.IOException;

public interface Webhook {

    void setContent(String content);

    void setUsername(String username);

    void setAvatarUrl(String avatarUrl);

    void setTts(boolean tts);

    void addEmbed(Embed embed);

    void send() throws IOException;
}
