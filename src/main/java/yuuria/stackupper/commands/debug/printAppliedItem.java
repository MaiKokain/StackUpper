package yuuria.stackupper.commands.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.StackUpperHelpers;

import java.util.Arrays;

public class printAppliedItem extends CommandBase {
    @Override
    public String getName() {
        return "printAppliedItem";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/stackupdebug printAppliedItem";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        StackUpper.LOGGER.info("called printAppliedItem");
        StackUpper.LOGGER.info("Applied Stack Map {}", StackUpperHelpers.appliedStackMap);
        StackUpper.LOGGER.info("Synced Config {}", Arrays.toString(StackUpperHelpers.trueConf));
    }
}
