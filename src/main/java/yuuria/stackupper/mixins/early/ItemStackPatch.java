package yuuria.stackupper.mixins.early;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.StackUpperHelpers;

@Mixin(ItemStack.class)
public class ItemStackPatch {
    @Shadow
    private int stackSize;

    @Shadow
    private CapabilityDispatcher capabilities;

    @ModifyReturnValue(method = "getMaxStackSize",at = @At("RETURN"))
    private int stackup$getMaxStackSize(int original)
    {
        int[] conf = StackUpperHelpers.trueConf;
        if (conf == null || conf.length < 4) return original;

        boolean advFilter = conf[0] != 0;
        boolean stackIgnoreAdvFilter = conf[1] != 0;
        int stack16 = conf[2];
        int stack64 = conf[3];

        if (!advFilter) {
            return switch (original) {
                case 16 -> stack16;
                case 64 -> stack64;
                default -> original;
            };
        }

        int override = StackUpperHelpers.appliedStackMap.get(((ItemStack) (Object) this).getItem());
        if (override == 0 && stackIgnoreAdvFilter) {
            return switch (original) {
                case 16 -> stack16;
                case 64 -> stack64;
                default -> original;
            };
        }
        return override == 0 ? original : override;
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;itemDamage:I", shift = At.Shift.AFTER, opcode = Opcodes.PUTFIELD, unsafe = true))
    private void stackup$fixNBTGet(NBTTagCompound compound, CallbackInfo ci)
    {
        this.stackSize = compound.getInteger("Count");
    }

    @WrapMethod(method = "writeToNBT")
    private NBTTagCompound stackup$fixNBTSet(NBTTagCompound nbt, Operation<NBTTagCompound> original)
    {
        ItemStack this0 = (ItemStack) (Object) this;
        ResourceLocation location = Item.REGISTRY.getNameForObject(this0.getItem());
        nbt.setString("id", location == null ? "minecraft:air" : location.toString());
        nbt.setInteger("Count", this.stackSize);
        nbt.setShort("Damage", (short) this0.getMetadata());

        if (this0.getTagCompound() != null) {
            nbt.setTag("tag", this0.getTagCompound());
        }
        if (this.capabilities != null) {
            NBTTagCompound caps = this.capabilities.serializeNBT();
            if (!caps.isEmpty()) {
                nbt.setTag("ForgeCaps", caps);
            }
        }
        return nbt;
    }
}
