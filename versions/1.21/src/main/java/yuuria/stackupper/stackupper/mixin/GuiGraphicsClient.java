package yuuria.stackupper.stackupper.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuria.stackupper.stackupper.Constants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

@Mixin(value = GuiGraphics.class, remap = false)
public abstract class GuiGraphicsClient {
    @Shadow @Deprecated protected abstract void flushIfUnmanaged();

    @Shadow @Final private PoseStack pose;
    @Shadow @Final private MultiBufferSource.BufferSource bufferSource;
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
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V")
    )
    private void PoseStackTranslate(PoseStack instance, float x, float y, float z, Operation<Void> original) {}

    @WrapOperation(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I")
    )
    private int NoDrawStr(GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow, Operation<Integer> original)
    {
        return 0;
    }

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", ordinal = 0
            )
    )
    private void renderText(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci)
    {
        String text_ = text == null ? getStringForBigStackCount(stack.getCount()) : text;
        float scale = (float) calculateStringScale(font, text_);
        float i_scale = 1/scale;
        this.pose.scale(scale, scale, 1);

        float custom_X = (x + 16) * i_scale - font.width(text_);
        float custom_Y = (y + 16) * i_scale - font.lineHeight;

        this.pose.translate(0.0F, 0.0F, 200.0F);
        font.drawInBatch(
                text_,
                custom_X,
                custom_Y,
                16777215,
                true,
                this.pose.last().pose(),
                this.bufferSource,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );
        this.flushIfUnmanaged();
    }
}
