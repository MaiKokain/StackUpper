package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import yuuria.stackupper.configlibrary.ConfigLibrary;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpperConfig;

public class ReloadCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        var builder = Commands.literal("reload").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("reparse_file", BoolArgumentType.bool())
                        .executes(c -> reload(c, BoolArgumentType.getBool(c, "reparse_file")))
        );
        ctx.then(builder);
    }

    private static int reload(CommandContext<CommandSourceStack> ctx, boolean recheckFilesCache)
    {
        if (!StackUpperConfig.CONFIG.enableScripting.get()) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Scripting is disabled").withStyle(ChatFormatting.RED), false);
            } else {
                Constants.logger.error("Scripting is disabled ");
            }
            return Command.SINGLE_SUCCESS;
        }

        if (recheckFilesCache) ConfigLibrary.addFile(Constants.StackUpperConfigRuleset, true);
        if (recheckFilesCache && Constant.FilesArray.isEmpty()) {
            Constants.logger.error("Re-cached files, but array is still empty");
            return Command.SINGLE_SUCCESS;
        }

        if (!Constant.ItemCollection.isEmpty()) Constant.ItemCollection.clear();
        ConfigLibrary.Start();
        StackSupplier.updateMaxStack();
        return Command.SINGLE_SUCCESS;
    }
}
