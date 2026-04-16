package yuuria.stackupper.stackupper.mixins.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

@Mixin(GuiGraphicsExtractor.class)
public abstract class FixItemDisplay {
    @Shadow
    public abstract void text(Font font, @Nullable String str, int x, int y, int color, boolean dropShadow);

    @Shadow
    public abstract Matrix3x2fStack pose();

    private static final DecimalFormat BILLION_FORMAT  = new DecimalFormat("#.##B");
    private static final DecimalFormat MILLION_FORMAT  = new DecimalFormat("#.##M");
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");

    private static String getStringForBigStackCount(int count)
    {
        var decimal = new BigDecimal(count).round(new MathContext(3));
        var val = decimal.doubleValue();

        if (val >= 1_000_000_000)
            return BILLION_FORMAT.format(val / 1_000_000_000);
        else if (val >= 1_000_000)
            return MILLION_FORMAT.format(val / 1_000_000);
        else if (val >= 1_000)
            return THOUSAND_FORMAT.format(val / 1_000);

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

    @WrapMethod(method = "itemCount", remap = false)
    private void renderCount(Font font, ItemStack itemStack, int x, int y, String countText, Operation<Void> original)
    {
        if (itemStack.getCount() != 1 || countText != null) {
            var pose_stack = this.pose();
            String amount = countText == null ? getStringForBigStackCount(itemStack.getCount()) : countText;

            float scale = (float) calculateStringScale(font, amount);
            float drawX = (x + 19 - 2) * (1/scale) - font.width(amount);
            float drawY = (y + 6 + 3) * (1/scale);
            drawX = Math.round(drawX);
            drawY = Math.round(drawY);


            pose_stack.pushMatrix();
            pose_stack.scale(scale);
            this.text(font, amount, (int) drawX, (int) drawY, -1, true);
            pose_stack.popMatrix();
        }
    }
}
