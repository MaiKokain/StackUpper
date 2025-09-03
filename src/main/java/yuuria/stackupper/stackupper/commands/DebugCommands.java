package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;

public class DebugCommands {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        ctx.then(Commands.literal("print_item_collection").executes(DebugCommands::print_item_collection));
        ctx.then(Commands.literal("print_files_array").executes(DebugCommands::print_files_array));
        ctx.then(Commands.literal("print_highest_stack").executes(DebugCommands::highest_stack_size));

    }

    private static int highest_stack_size(CommandContext<CommandSourceStack> ctx)
    {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(String.valueOf(StackSupplier.getMaxStack())), false);
        }
        Constants.logger.info(String.valueOf(StackSupplier.getMaxStack()));
        return Command.SINGLE_SUCCESS;
    }

    private static int print_item_collection(CommandContext<CommandSourceStack> ctx)
    {
        if (Constant.ItemCollection.isEmpty()) {
            Constants.logger.error("ItemCollection is empty.");
            return Command.SINGLE_SUCCESS;
        }

        Constant.ItemCollection.forEach((s, v) -> {
            Constants.logger.info("{} = {}", s.toString(), v.toString());
        });
        return Command.SINGLE_SUCCESS;
    }

    private static int print_files_array(CommandContext<CommandSourceStack> ctx)
    {
        if (Constant.FilesArray.isEmpty()) {
            Constants.logger.error("FilesArray is empty");
            return Command.SINGLE_SUCCESS;
        }

        Constant.FilesArray.forEach(f -> {
            Constants.logger.info("filesarray: {}", f.getAbsolutePath());
        });

        return Command.SINGLE_SUCCESS;
    }

}
