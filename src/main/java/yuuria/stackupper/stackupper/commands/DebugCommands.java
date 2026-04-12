package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.items.ItemStackHandler;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;

public class DebugCommands {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        ctx.then(Commands.literal("print_item_collection").executes(DebugCommands::print_item_collection));
        ctx.then(Commands.literal("print_files_array").executes(DebugCommands::print_files_array));
        ctx.then(Commands.literal("print_highest_stack").executes(DebugCommands::highest_stack_size));
        ctx.then(Commands.literal("print_sync_stack_size").executes(DebugCommands::print_sync_stack_size));
        ctx.then(Commands.literal("call_update_stack_supplier").executes(DebugCommands::call_update_stack_supplier));
    }

    private static int call_update_stack_supplier(CommandContext<CommandSourceStack> ctx)
    {
        Constants.logger.info("old s {}", StackSupplier.getMaxStack());
        StackSupplier.updateMaxStack();
        Constants.logger.info("new s {}", StackSupplier.getMaxStack());
        return Command.SINGLE_SUCCESS;
    }
    private static int print_sync_stack_size(CommandContext<CommandSourceStack> ctx)
    {
        if (Constants.SyncedServerSizes.isEmpty()) {
            ctx.getSource().sendSystemMessage(Component.literal("SyncedServerSizes is empty!"));
            return Command.SINGLE_SUCCESS;
        }
        Constants.SyncedServerSizes.forEach((s, v) -> {
            Constants.logger.info("{} = {}", s, v);
        });
        return Command.SINGLE_SUCCESS;
    }

    private static int highest_stack_size(CommandContext<CommandSourceStack> ctx)
    {
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
