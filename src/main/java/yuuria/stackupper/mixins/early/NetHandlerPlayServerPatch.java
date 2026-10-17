package yuuria.stackupper.mixins.early;

import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import yuuria.stackupper.StackUpperHelpers;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerPatch {
    @ModifyConstant(
            method = "processCreativeInventoryAction",
            constant = @Constant(intValue = 64, ordinal = 0)
    )
    private int stackup$creativeMaxStack(int orig)
    {
        return StackUpperHelpers.getMaxStackSize() / 2;
    }
}
