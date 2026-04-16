package yuuria.stackupper.stackupper.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilMenu.class, remap = false)
public class AnvilMenuMixin {
    @Shadow
    public int repairItemCountCost;

    @WrapOperation(
            method = "onTake",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 2),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 3)
            }
    )
    void onTake(Container instance, int slot, ItemStack carried, Operation<Void> original)
    {
        ItemStack left = instance.getItem(0);
        ItemStack right = instance.getItem(1);
        int shrinksize = 1;
        if (right != ItemStack.EMPTY && left.getCount() < right.getCount()) {
            shrinksize = left.getCount();
        } else if (right != ItemStack.EMPTY && left.getCount() > right.getCount()) {
            shrinksize = right.getCount();
        } else if (right == ItemStack.EMPTY && left.getCount() != 1) {
            shrinksize = left.getCount();
        }

        left.shrink(shrinksize);
        if (right != ItemStack.EMPTY) right.shrink(shrinksize);
    }

    @Inject(method = "createResultInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void adjustResultStackSize(CallbackInfo ci) {
        ItemStack left = ((ItemCombinerMenu) (Object) this).getSlot(0).getItem();
        ItemStack right = ((ItemCombinerMenu) (Object) this).getSlot(1).getItem();
        ItemStack result = ((ItemCombinerMenu) (Object) this).getSlot(((ItemCombinerMenu) (Object) this).getResultSlot()).getItem();

        if (!left.isEmpty() && !result.isEmpty()) {
            int batchSize = left.getCount();

            if (!right.isEmpty() && this.repairItemCountCost <= 0) {
                batchSize = Math.min(left.getCount(), right.getCount());
            }

            result.setCount(batchSize);
        }
    }
}
