package yuuria.stackupper.mixins.early;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;

import java.io.IOException;

@Mixin(PacketBuffer.class)
public class PacketBufferPatch {
    @WrapMethod(method = "readItemStack")
    private ItemStack stackup$readItemStack(Operation<ItemStack> original) throws IOException {
        PacketBuffer buffer = (PacketBuffer) (Object) this;
        int id = buffer.readShort();

        if (id < 0) return ItemStack.EMPTY;

        int count = buffer.readByte();
        if (count == -42) count = buffer.readInt();
        int meta = buffer.readShort();
        ItemStack itemStack = new ItemStack(Item.getItemById(id), count, meta);
        itemStack.getItem().readNBTShareTag(itemStack, buffer.readCompoundTag());
        return itemStack;
    }

    @WrapMethod(method = "writeItemStack")
    private PacketBuffer stackup$writeItemStack(ItemStack stack, Operation<PacketBuffer> original)
    {
        PacketBuffer buffer = (PacketBuffer) (Object) this;
        if (stack.isEmpty()) {
            buffer.writeShort(-1);
            return buffer;
        }

        buffer.writeShort(Item.getIdFromItem(stack.getItem()));
        if (stack.getCount() >= 0 && stack.getCount() <= 64) {
            buffer.writeByte(stack.getCount());
        } else {
            buffer.writeByte(-42);
            buffer.writeInt(stack.getCount());
        }
        buffer.writeShort(stack.getMetadata());
        NBTTagCompound tag = null;

        if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
            tag = stack.getItem().getNBTShareTag(stack);
        }

        buffer.writeCompoundTag(tag);
        return buffer;
    }
}
