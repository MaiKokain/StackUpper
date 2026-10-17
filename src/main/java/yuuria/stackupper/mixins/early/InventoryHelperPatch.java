package yuuria.stackupper.mixins.early;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yuuria.stackupper.StackUpperHelpers;

@Mixin(InventoryHelper.class)
public class InventoryHelperPatch {
    /**
     * @author Yuuria
     * @reason idk
     */
    @Overwrite
    public static void spawnItemStack(World world, double x, double y, double z, ItemStack itemStack)
    {
        float xOffset = StackUpperHelpers.RANDOM.nextFloat() * 0.8F + 0.1F;
        float yOffset = StackUpperHelpers.RANDOM.nextFloat() * 0.8F + 0.1F;
        float zOffset = StackUpperHelpers.RANDOM.nextFloat() * 0.8F + 0.1F;

        int minStackSize = (itemStack.getMaxStackSize() + 7) / 8;
        int maxStackSize = (itemStack.getMaxStackSize() + 1) / 2;

        if (itemStack.getCount() <= 8) {
            minStackSize = itemStack.getCount();
            maxStackSize = itemStack.getCount();
        }

        while (!itemStack.isEmpty())
        {
            EntityItem entityItem = new EntityItem(
                    world,
                    x + xOffset,
                    y + yOffset,
                    z + zOffset,
                    itemStack.splitStack(StackUpperHelpers.RANDOM.nextInt(maxStackSize - minStackSize + 1) + minStackSize)
            );
            entityItem.motionX = StackUpperHelpers.RANDOM.nextGaussian() * 0.05D;
            entityItem.motionY = StackUpperHelpers.RANDOM.nextGaussian() * 0.05D + 0.2D;
            entityItem.motionZ = StackUpperHelpers.RANDOM.nextGaussian() * 0.05D;
            world.spawnEntity(entityItem);
        }
    }
}
