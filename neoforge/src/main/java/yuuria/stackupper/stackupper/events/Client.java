package yuuria.stackupper.stackupper.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import yuuria.stackupper.stackupper.StackUpper;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = StackUpper.MODID)
public class Client {
    public static final DecimalFormat TOOLTIP_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static public void showCount(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.getCount() > 1_000) {
            event.getToolTip()
                    .add(
                            1,
                            Component.literal("Exact: ")
                                    .append(
                                            Component.literal(TOOLTIP_FORMAT.format(stack.getCount())).withStyle(ChatFormatting.AQUA)
                                    )
                    );
        }
    }
}
