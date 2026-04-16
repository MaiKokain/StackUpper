package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import yuuria.stackupper.stackupper.StackUpper;

@EventBusSubscriber(modid = StackUpper.MODID)
public class CommandRegisters {
    @SubscribeEvent
    public static void registerCommand(final RegisterCommandsEvent event)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stackupper");
        ReloadCommand.register(command);
        event.getDispatcher().register(command);
    }

    @SubscribeEvent
    public static void registerClientCommands(final RegisterClientCommandsEvent event)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stackupper_client");
        DebugCommand.register(command);
        event.getDispatcher().register(command);
    }
}
