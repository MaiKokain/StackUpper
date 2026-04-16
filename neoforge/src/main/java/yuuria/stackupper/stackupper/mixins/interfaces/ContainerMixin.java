package yuuria.stackupper.stackupper.mixins.interfaces;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = Container.class, remap = false)
public interface ContainerMixin {
    @Inject(method = {"getMaxStackSize()I", "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I"}, at = @At("HEAD"), cancellable = true)
    default void fixContainerMixin(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(StackSupplier.getMaxStack());
    }
}
