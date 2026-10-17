package yuuria.stackupper.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yuuria.stackupper.client.StackUpperTextGenerator;

import java.text.NumberFormat;

public class StackUpperClientProxy extends StackUpperCommonProxy {
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event)
    {
        FontRenderer renderer = event.getItemStack().getItem().getFontRenderer(event.getItemStack());
        if (renderer == null)
        {
            renderer = Minecraft.getMinecraft().fontRenderer;
            if (renderer == null) return;
        }

        String count = Integer.toString(event.getItemStack().getCount());
        StackUpperTextGenerator.AbbreviationResult countA = StackUpperTextGenerator.abbreviate(renderer, count, 16, true);
        if (countA.isAbbreviated()) event.getToolTip().add("Count: " + NumberFormat.getInstance().format(event.getItemStack().getCount()));
    }

    @Override
    public int getCurrentScaleFactor()
    {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    }
}
