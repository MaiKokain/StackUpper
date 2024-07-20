package yuria.stackupper.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import yuria.stackupper.StackUpper;
import yuria.stackupper.config.StackSize;

import java.util.function.Function;

@Mixin(value = ExtraCodecs.class, remap = false)
public abstract class FunIntRangePatch {
    private static int patched99 = 0;

    @Shadow
    protected static Codec<Integer> intRangeWithMessage(int min, int max, Function<Integer, String> errorMessage) {
        return null;
    }

    @WrapMethod(
            method = "intRange"
    )
    private static Codec<Integer> intRange(int min, int max, Operation<Codec<Integer>> original)
    {
        int i;
//                = max == 99 ? Integer.MAX_VALUE : max;
        // forced to be 2 so future max 99 doesn't get returned StackSize
        if (max == 99 && patched99 != 2) {
            i = StackSize.getMaxStackSize();
            patched99++;
        } else {
            i = max;
        }
        StackUpper.LOGGER.debug("IntRage was called {};{}", min, i);
        return intRangeWithMessage(min, i, m -> "Value must be within range ["+min+";"+i+"]: " + m);
    }
}
