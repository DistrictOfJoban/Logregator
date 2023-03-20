package com.lx.logregator.data;

public enum MTRActionType {
    CREATE("created", ":eight_spoked_asterisk:"),
    EDIT("modified", ":pencil2:"),
    REMOVE("deleted", ":x:");

    private final String action;
    private final String emojiRepresentation;
    MTRActionType(String str, String emoji) {
        this.action = str;
        this.emojiRepresentation = emoji;
    }

    public String getAction() {
        return action;
    }
    public String getEmoji() {
        return emojiRepresentation;
    }
}
