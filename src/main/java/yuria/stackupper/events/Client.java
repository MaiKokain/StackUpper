package yuria.stackupper.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import yuria.stackupper.Constants;
import yuria.stackupper.StackUpper;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID)
public class Client {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static public void showTrueCount(ItemTooltipEvent event) {
        var stack = event.getItemStack();

        if (stack.getCount() > Constants.THOUSAND) {
            event.getToolTip()
                    .add(1,
                            Component.translatable("stackupper.exact.count",
                                    Component.literal(Constants.TOOLTIP_NUMBER_FORMAT.format(stack.getCount())).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY)
                    );
        }
    }
}
