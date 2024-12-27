package com.lx862.logregator.data;

public enum MTRActionType {
    CREATE("created", ":eight_spoked_asterisk:"),
    EDIT("modified", ":pencil2:"),
    REMOVE("deleted", ":x:");

    MTRActionType(String str, String emoji) {
        this.action = str;
        this.emojiRepresentation = emoji;
    }

    public final String action;
    public final String emojiRepresentation;
}
