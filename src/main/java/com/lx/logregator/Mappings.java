package com.lx.logregator;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class Mappings {
    public static MutableText literalText(String content) {
        #if MC_VERSION >= "11900"
            return Text.literal(content);
        #else
            return new net.minecraft.text.LiteralText(content);
        #endif
    }
}
