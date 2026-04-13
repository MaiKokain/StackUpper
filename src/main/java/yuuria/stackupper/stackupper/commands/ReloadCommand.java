package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import yuuria.stackupper.configlibrary.ConfigLibrary;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpperConfig;
import yuuria.stackupper.stackupper.network.SyncStackSizesPayload;

public class ReloadCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        var builder = Commands.literal("reload").requires(c -> c.hasPermission(4));

        builder.then(
                Commands.argument("reparse_file", BoolArgumentType.bool())
                        .executes(c -> reload(c, BoolArgumentType.getBool(c, "reparse_file")))
        );
        ctx.then(builder);
    }

    private static int reload(CommandContext<CommandSourceStack> ctx, boolean recheckFilesCache)
    {
        if (!StackUpperConfig.CONFIG.enableScripting.get()) {
            ctx.getSource().sendSystemMessage(Component.literal("Scripting is disabled").withStyle(ChatFormatting.RED));
            return Command.SINGLE_SUCCESS;
        }

        if (recheckFilesCache) ConfigLibrary.addFile(Constants.StackUpperConfigRuleset, true);
        if (recheckFilesCache && Constant.FilesArray.isEmpty()) {
            Constants.logger.error("Re-cached files, however array is still empty");
            ctx.getSource().sendSystemMessage(Component.literal("Failed to reload: Config array is empty.").withStyle(ChatFormatting.RED));
            return Command.SINGLE_SUCCESS;
        }


//        PacketDistributor.sendToAllPlayers(new SyncStackSizesPayload( Constants.generateSyncHashMap()));
        StackUpperConfig.syncAndBroadcastToClients();

        ctx.getSource().sendSystemMessage(Component.literal("Stack sizes reloaded and synced to all players.").withStyle(ChatFormatting.GREEN));

        return Command.SINGLE_SUCCESS;
    }
}
