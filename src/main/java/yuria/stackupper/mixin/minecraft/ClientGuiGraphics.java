package yuria.stackupper.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuria.stackupper.Constants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

@Mixin(value = GuiGraphics.class, remap = false)
public abstract class ClientGuiGraphics {
    @Shadow public abstract int drawString(Font font, @Nullable String text, int x, int y, int color);
    @Shadow public abstract int drawString(Font font, @Nullable String text, int x, int y, int color, boolean dropShadow);


    private static final DecimalFormat BILLION_FORMAT  = new DecimalFormat("#.##B");
    private static final DecimalFormat MILLION_FORMAT  = new DecimalFormat("#.##M");
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");

    private static String getStringForBigStackCount(int count)
    {
        var decimal = new BigDecimal(count).round(new MathContext(3));
        var val = decimal.doubleValue();

        if (val >= Constants.BILLION)
            return BILLION_FORMAT.format(val / Constants.BILLION);
        else if (val >= Constants.MILLION)
            return MILLION_FORMAT.format(val / Constants.MILLION);
        else if (val >= Constants.THOUSAND)
            return THOUSAND_FORMAT.format(val / Constants.THOUSAND);

        return String.valueOf(count);
    }

    private static double calculateStringScale(Font font, String countString)
    {
        var width = font.width(countString);

        if (width < 16)
            return 1.0;
        else
            return 16.0 / width;
    }

    @WrapOperation(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemCount(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
            )
    )
    private void renderText(GuiGraphics instance, Font font, ItemStack stack, int x, int y, String text, Operation<Void> original)
    {
        if (text != null || stack.getCount() != 1) {
            var poseStack = instance.pose();
            String text_ = text == null ? getStringForBigStackCount(stack.getCount()) : text;

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            float scale = (float) calculateStringScale(font, text_);
            float inverseScale = 1 / scale;
            float drawX = (x + 16) * inverseScale - font.width(text_);
            float drawY = (y + 16) * inverseScale - font.lineHeight;

            poseStack.pushPose();
            poseStack.scale(scale, scale, 1);
            poseStack.translate(drawX, drawY, 200.0F);


            font.drawInBatch(
                    text_,
                    0,
                    0,
                    16777215,
                    true,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );
            bufferSource.endBatch();
            poseStack.popPose();
        }
    }

}
