package yuuria.stackupper.stackupper.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;
import yuuria.stackupper.stackupper.StackUpper;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @ModifyReturnValue(
            method = "getMaxStackSize",
            at = @At("RETURN")
    )
    public int modifyGetMaxStack(int orig)
    {
        if (!StackUpperCommonConfig.CONFIG_SPEC.isLoaded()) {
            return orig;
        }

        if (!StackUpperCommonConfig.SyncedServerConfig.CURRENT.enable_script_feature()) {
            if (orig == 1) return orig;
            if (!StackUpperCommonConfig.SyncedServerConfig.CURRENT.enable_stacks_of_16() && orig == 16) return orig;
            return StackUpperCommonConfig.SyncedServerConfig.CURRENT.global_max_stack();
        }

        if (!StackUpper.SyncedServerItems.isEmpty() && StackUpper.SyncedServerItems.containsKey(this.getItem())) {
            return StackUpper.SyncedServerItems.get(this.getItem());
        }
        return orig == 1 ? orig : StackUpperCommonConfig.SyncedServerConfig.CURRENT.global_max_stack();
    }

    @WrapOperation(
            method = "lambda$static$1",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
            )
    )
    private static Codec<Integer> fixCodecRange(int min, int max, Operation<Codec<Integer>> orig)
    {
        return orig.call(min, Integer.MAX_VALUE);
    }
}
