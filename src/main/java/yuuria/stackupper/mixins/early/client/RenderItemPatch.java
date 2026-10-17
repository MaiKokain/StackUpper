package yuuria.stackupper.mixins.early.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.client.StackUpperTextGenerator;

@Mixin(RenderItem.class)
public class RenderItemPatch {
    @WrapOperation(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int stackup$drawStringWithShadow(FontRenderer instance, String text, float x, float y, int color, Operation<Integer> original)
    {
        if (text.isEmpty()) return 0;

        float xOffset = 19 - 2 - instance.getStringWidth(text);
        float yOffset = 6 + 3;

        x -= xOffset;
        y -= yOffset;
        StackUpperTextGenerator.AbbreviationResult result = StackUpperTextGenerator.abbreviate(instance, text, 16, false);
        float scaleDiff = result.getScaleFactor();

        x += 16 - (instance.getStringWidth(result.getText()) * scaleDiff);
        y += 16 - (8 * scaleDiff);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0f);
        GlStateManager.scale(scaleDiff, scaleDiff, 1f);
        GlStateManager.translate(-x, -y, 0.0f);
        int t = instance.drawStringWithShadow(result.getText(), x, y, color);
        GlStateManager.popMatrix();
        return t;
    }
}
