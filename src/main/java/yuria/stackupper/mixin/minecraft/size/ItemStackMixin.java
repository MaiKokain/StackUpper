package yuria.stackupper.mixin.minecraft.size;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuria.stackupper.StackUpper;
import yuria.sul.ast.AssignOperation;
import yuria.sul.ast.item.ItemProperty;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {
    @ModifyReturnValue(
            method = "getMaxStackSize",
            at = @At("RETURN")
    )
    private int getMaxStackFalse(int orig)
    {
        ItemStack itemStack = (ItemStack) (Object)this;
        ItemProperty itemProperty = StackUpper.itemCollection.get(itemStack.getItem());

        if (itemProperty == null) return orig;

        if (itemProperty.assignOperation == AssignOperation.EQUAL) return (int) Math.max(itemProperty.doOpBy, 1);

        long toSet = itemProperty.assignOperation.apply(itemProperty.doOpBy, (long) orig).longValue();

        return (int) Math.min(Math.max(toSet, 1), Integer.MAX_VALUE);
    }

    @WrapOperation(
            method = "lambda$static$1",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;")
    )
    private static Codec<Integer> ItemStackCodecRangePatch(int pMin, int pMax, Operation<Codec<Integer>> original)
    {
        return original.call(pMin, Integer.MAX_VALUE);
    }
}
