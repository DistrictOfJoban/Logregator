package com.lx.logregator.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

public class Area {
    private BlockPos pos1;
    private BlockPos pos2;

    public Area(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Area(JsonObject jsonObject) {
        JsonArray pos1Json = jsonObject.get("pos1").getAsJsonArray();
        JsonArray pos2Json = jsonObject.get("pos2").getAsJsonArray();

        BlockPos pos1 = new BlockPos(pos1Json.get(0).getAsInt(), pos1Json.get(1).getAsInt(), pos1Json.get(2).getAsInt());
        BlockPos pos2 = new BlockPos(pos2Json.get(0).getAsInt(), pos2Json.get(1).getAsInt(), pos2Json.get(2).getAsInt());
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isInArea(BlockPos pos) {
        return (between(pos1.getX(), pos2.getX(), pos.getX()))
                && (between(pos1.getY(), pos2.getY(), pos.getY()))
                && (between(pos1.getZ(), pos2.getZ(), pos.getZ()));
    }

    private boolean between(int n1, int n2, int value) {
        return value >= Math.min(n1, n2) && value <= Math.max(n1, n2);
    }
}
