package yuuria.stackupper.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.server.command.CommandTreeBase;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.StackUpperConfig;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.commands.debug.getItemStackInfo;
import yuuria.stackupper.commands.debug.printAppliedItem;
import yuuria.stackupper.network.SyncItemData;

public class StackUpperCommand extends CommandTreeBase {
    public StackUpperCommand()
    {

        this.addSubcommand(new StackUpReloadCommand());
        this.addSubcommand(new getItemStackInfo());
        this.addSubcommand(new printAppliedItem());
    }

    @Override
    public String getName() {
        return "stackupper";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/stackup [reload]";
    }

    public class StackUpReloadCommand extends CommandBase {

        @Override
        public String getName() {
            return "reload";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/stackupper reload";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            StackUpper.CONFIG = StackUpperConfig.read();
            StackUpper.CONFIG.save();
            StackUpper.process(ForgeRegistries.ITEMS);
            if (server != null) server.addScheduledTask(() -> {
                if (server.isDedicatedServer()) StackUpperHelpers.trueConf = StackUpperHelpers.generateConfigInt();
                StackUpper.INSTANCE.sendToAll(new SyncItemData(StackUpperHelpers.generateConfigInt(), StackUpperHelpers.appliedStackMap));
            });
        }
    }
}
