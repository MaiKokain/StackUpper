package yuuria.stackupper.stackupper;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import yuuria.stackupper.stackupper.commands.DebugCommands;
import yuuria.stackupper.stackupper.commands.GenerateDataIndexCommand;
import yuuria.stackupper.stackupper.commands.ReloadCommand;

@EventBusSubscriber(modid = "stackupper")
public class StackUpperCommand {

    @SubscribeEvent
    public static void onRegisterCommand(final RegisterCommandsEvent event)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stackupper");

        String debugProp = System.getProperty("stackupper.debug");
        if (debugProp != null)
        {
            DebugCommands.register(command);
        }
        ReloadCommand.register(command);
        GenerateDataIndexCommand.register(command);
        event.getDispatcher().register(command);
    }
}
