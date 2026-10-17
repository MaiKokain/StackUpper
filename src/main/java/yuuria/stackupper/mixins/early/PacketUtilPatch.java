package yuuria.stackupper.mixins.early;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.PacketUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PacketUtil.class)
public class PacketUtilPatch {
    @WrapMethod(method = "writeItemStackFromClientToServer", remap = false)
    private static void stackup$writeItemStackFromClientToServer(PacketBuffer buffer, ItemStack stack, Operation<Void> original)
    {
        if (stack.isEmpty())
        {
            buffer.writeShort(-1);
        }
        else
        {
            buffer.writeShort(Item.getIdFromItem(stack.getItem()));
            if (stack.getCount() >= 0 && stack.getCount() <= 64) {
                buffer.writeByte(stack.getCount());
            } else {
                buffer.writeByte(-42);
                buffer.writeInt(stack.getCount());
            }
            buffer.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag())
            {
                nbttagcompound = stack.getTagCompound();
            }

            buffer.writeCompoundTag(nbttagcompound);
        }
    }
}
