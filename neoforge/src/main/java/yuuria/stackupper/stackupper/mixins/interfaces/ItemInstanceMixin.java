package yuuria.stackupper.stackupper.mixins.interfaces;

import net.minecraft.world.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = ItemInstance.class, remap = false)
public interface ItemInstanceMixin {
    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
    default void fixItemInstanceInterface(CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(StackSupplier.getMaxStack());
    }
}
