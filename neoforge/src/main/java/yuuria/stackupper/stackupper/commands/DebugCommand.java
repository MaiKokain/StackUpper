package yuuria.stackupper.stackupper.commands;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.config.StackUpperClientConfig;

public class DebugCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        var builder = Commands.literal("debug").requires(c -> StackUpperClientConfig.CONFIG.enable_debug_commands.get());
        builder.then(Commands.literal("print_synced_item_data").executes(DebugCommand::print_synced_item_data));
        ctx.then(builder);
    }

    private static int print_synced_item_data(CommandContext<CommandSourceStack> ctx)
    {
        StackUpper.SyncedServerItems.forEach((i,v ) -> StackUpper.LOGGER.info("{} = {}", i, v));
        return 1;
    }
}
