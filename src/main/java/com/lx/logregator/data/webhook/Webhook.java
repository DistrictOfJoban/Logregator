package com.lx.logregator.data.webhook;

import com.lx.logregator.data.event.EventType;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Webhook {

    void setContent(String content);

    void setUsername(String username);

    void setAvatarUrl(String avatarUrl);

    void setTts(boolean tts);

    void addEmbed(Embed embed);

    void send(EventType eventType) throws IOException;
}
