package yuuria.stackupper.stackupper;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import yuuria.stackupper.stackupper.commands.DebugCommands;
import yuuria.stackupper.stackupper.commands.GenerateDataIndexCommand;
import yuuria.stackupper.stackupper.commands.ReloadCommand;

@EventBusSubscriber(modid = "stackupper", value = Dist.CLIENT)
public class StackUpperClientCommand {

    @SubscribeEvent
    public static void onRegisterCommand(final RegisterClientCommandsEvent event)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stackupper_client");

        String debugProp = System.getProperty("stackupper.debug");
        if (debugProp != null)
        {
            DebugCommands.register(command);
        }
        event.getDispatcher().register(command);
    }
}
