package yuuria.stackupper.stackupper.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import yuuria.stackupper.stackupper.Constants;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = "stackupper")
public class Client {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static public void showCount(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.getCount() > Constants.THOUSAND) {
            event.getToolTip()
                    .add(
                            1,
                            Component.literal("Exact: ")
                                    .append(
                                        Component.literal(Constants.TOOLTIP_FORMAT.format(stack.getCount())).withStyle(ChatFormatting.AQUA)
                                    )
                    );
        }
    }
}
