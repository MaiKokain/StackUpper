package yuria.stackupper.mixin.minecraft.size;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuria.stackupper.StackUpper;
import yuria.stackupper.StackUpperConfig;
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

        if (itemProperty == null) {
            if (orig == 1) {
                return orig;
            }
            return StackUpperConfig.CONFIG.maxStackGlobally.get();
        }

        if (itemProperty.assignOperation == AssignOperation.EQUAL) return (int) Math.max(itemProperty.doOpBy, 1);

        long assignOperationResponse = itemProperty.assignOperation.apply(itemProperty.doOpBy, (long) orig);

        return (int) Math.min(Math.max(assignOperationResponse, 1), Integer.MAX_VALUE);
    }

    @WrapOperation(
            method = "lambda$static$3",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;")
    )
    private static Codec<Integer> idekLOL(int pMin, int pMax, Operation<Codec<Integer>> original)
    {
        StackUpper.LOGGER.info("Codec Invoke @ item max, val: {} {}", pMin, pMax);
        return original.call(pMin, Integer.MAX_VALUE);
    }
}
