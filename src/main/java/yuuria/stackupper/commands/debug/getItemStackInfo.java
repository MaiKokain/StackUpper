package yuuria.stackupper.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import yuuria.stackupper.StackUpper;

public class getItemStackInfo extends CommandBase {
    @Override
    public String getName() {
        return "printItemStack";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/stackupdebug printItemStack";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
        ItemStack held = playerMP.getHeldItemMainhand();

        if (held.isEmpty()) {
            sender.sendMessage(new TextComponentString("Empty hand/ghost item"));
            return;
        }
        sender.sendMessage(new TextComponentString(
                held.getDisplayName() + " -> " + held.getCount() + " / " + held.getMaxStackSize()
        ));
        NBTTagCompound nbt = held.getTagCompound();
        if (nbt == null) { sender.sendMessage(new TextComponentString("no nbt")); return; }

        StackUpper.LOGGER.info(nbt.toString());
    }
}
