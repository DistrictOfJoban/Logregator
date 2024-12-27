package com.lx862.logregator.mixin;

import com.lx862.logregator.Util;
import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.webhook.DiscordEmbed;
import com.lx862.logregator.data.webhook.DiscordWebhook;
import com.lx862.logregator.data.webhook.Embed;
import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Mixin(RailwayData.class)
public class RailwayDataMixin {
    @Inject(method = "validateRails", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void validateRails(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, CallbackInfo ci, Set<BlockPos> railsToRemove, Set<BlockPos> railNodesToRemove) {
        if(railsToRemove.isEmpty() && railNodesToRemove.isEmpty()) return;
        RailwayData railwayData = RailwayData.getInstance(world);
        if(railwayData == null) return;

        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        Embed embed = new DiscordEmbed()
                .setTitle(":warning: Rail Validation Performed")
                .setDescription("The following rail is removed as they are deemed invalid:")
                .setTimestamp();

        // No other side of rail found (Break), removing:
        if(!railsToRemove.isEmpty()) {
            StringBuilder str = new StringBuilder();
            int i = 0;
            for(BlockPos pos : railsToRemove) {
                String nearestStn = Util.findNearestMTRStructure(railwayData, pos);
                str.append(String.format("%d. **[%d, %d, %d]**", i, pos.getX(), pos.getY(), pos.getZ())).append(nearestStn).append("\n");
            }
            embed.addField("Rail Node Broken", str.toString(), false);
        }

        // Rail node no longer exists (w/e realization), removing:
        if(!railNodesToRemove.isEmpty()) {
            StringBuilder str = new StringBuilder();
            int i = 0;
            for(BlockPos pos : railNodesToRemove) {
                String nearestStn = Util.findNearestMTRStructure(railwayData, pos);
                str.append(String.format("%d. **[%d, %d, %d]**", i, pos.getX(), pos.getY(), pos.getZ())).append(nearestStn).append("\n");
            }
            embed.addField("Cannot find expected rail node (!)", str.toString(), false);
        }

        webhook.addEmbed(embed);
        try {
            webhook.send();
        } catch (IOException ignored) {
        }
    }
}
