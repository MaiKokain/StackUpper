package yuuria.stackupper.stackupper.mixin;

import net.minecraft.world.Containers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = Containers.class, remap = false)
public class ContainersMixin {
    @ModifyConstant(
            method = "dropItemStack",
            constant = {@Constant(intValue = 21), @Constant(intValue = 10)}
    )
    private static int dropItemStackMixin(int value)
    {
        if (value == 1) return 1;
        int newStackSize = StackSupplier.getMaxStack();
        if (newStackSize < 64) return 64;

        return Math.max(value, value * newStackSize / 64);
    }
}
