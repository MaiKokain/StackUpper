package yuuria.stackupper.stackupper.mixins;

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
    private static int dropItemStack(int val) {
        if (val == 1) return 1;
        return Math.max(val, val * StackSupplier.getMaxStack() / 64);
    }
}
