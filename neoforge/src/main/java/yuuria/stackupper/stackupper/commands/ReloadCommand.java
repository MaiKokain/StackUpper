package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.configlibrary.StackUpperConfigLibrary;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;

public class ReloadCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        var builder = Commands.literal("reload")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));

        builder.then(
                Commands.argument("reparse_files", BoolArgumentType.bool())
                        .executes(c -> reload(c, BoolArgumentType.getBool(c, "reparse_files")))
        );
        ctx.then(builder);
    }

    private static int reload(CommandContext<CommandSourceStack> ctx, boolean recheck_files_cache)
    {
        if (!StackUpperCommonConfig.CONFIG.enable_scripting.get()) {
            ctx.getSource().sendFailure(Component.literal("Scripting is disabled."));
            return 0;
        }
        if (recheck_files_cache) StackUpperConfigLibrary.addFile(StackUpper.StackUpperConfigRuleset, true);
        if (recheck_files_cache && StackUpperConfigLibrary.filesToParse.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Failed to reload stackupper scripts, There isn't any file to parse!"));
            return 0;
        }

        StackUpperCommonConfig.syncAndBroadcast();
        ctx.getSource().sendSuccess(() -> Component.literal("Stack size reloaded and synced"), true);
        return Command.SINGLE_SUCCESS;
    }
}
