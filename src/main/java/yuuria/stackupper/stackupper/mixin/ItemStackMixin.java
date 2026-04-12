package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;
import yuuria.stackupper.configlibrary.ast.AssignOperator;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackUpperConfig;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @ModifyReturnValue(
            method = "getMaxStackSize",
            at = @At("RETURN")
    )
    private int fixGetMaxStackSize(int orig)
    {
        if (!StackUpperConfig.CONFIG_SPEC.isLoaded()) {
            return orig;
        }

        if (!StackUpperConfig.CONFIG.enableScripting.get()) {
            if (orig == 1) return orig;
            return StackUpperConfig.CONFIG.maxStackGlobally.get();
        }

        Item item = this.getItem();

        if (!Constants.SyncedServerSizes.isEmpty() && Constants.SyncedServerSizes.containsKey(item)) {
            return Constants.SyncedServerSizes.get(item);
        }  else if (!Constant.ItemCollection.isEmpty() && Constant.ItemCollection.containsKey(this.getItem())) {
            Property property = Constant.ItemCollection.get(this.getItem());
            if (property == null) return orig;

            long returnStackedSize;
            if (property.assignOperator != AssignOperator.EQUAL) {
                returnStackedSize = property.assignOperator.apply(property.assignedBy, property.origStackSize);
            } else {
                returnStackedSize = property.assignOperator.apply(property.assignedBy);
            }
            return (int) Math.min(Math.max(returnStackedSize,1), Integer.MAX_VALUE);
        }

        return orig;
    }

    @WrapOperation(
            //? if = 1.21 {
             method = "lambda$static$3",
            //?} else
            //method = "lambda$static$1",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
            )
    )
    private static Codec<Integer> fixItemStackCodecRange(int min, int max, Operation<Codec<Integer>> original)
    {
        return original.call(min, Integer.MAX_VALUE);
    }
}
