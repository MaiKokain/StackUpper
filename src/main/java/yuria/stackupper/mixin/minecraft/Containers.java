package yuria.stackupper.mixin.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import yuria.stackupper.config.StackSize;

@Mixin(value = net.minecraft.world.Containers.class, remap = false)
public class Containers {
    @ModifyConstant(method = "dropItemStack", constant = {@Constant(intValue = 21), @Constant(intValue = 10)})
    private static int dropItemStack(int value) {
        return StackSize.getMaxStackSize();
    }
}
