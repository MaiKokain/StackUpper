package yuria.stackupper.mixin.minecraft.size;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import yuria.stackupper.config.StackSize;

@Mixin(value = Item.class, remap = false)
public class ItemMixin {

    @WrapMethod(
            method = "getDefaultMaxStackSize"
    )
    public int getDefaultMaxStackSize(Operation<Integer> original)
    {
        return StackSize.getMaxStackSize();
    }
}
