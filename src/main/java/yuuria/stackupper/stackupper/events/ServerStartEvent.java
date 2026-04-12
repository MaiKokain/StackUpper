package yuuria.stackupper.stackupper.events;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import yuuria.stackupper.configlibrary.ConfigLibrary;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.StackUpperConfig;

@EventBusSubscriber(modid = "stackupper")
public class ServerStartEvent {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onServerStart(ServerStartingEvent event)
    {
        StackUpper.checkAndCreateFolder();
        if (StackUpperConfig.CONFIG.enableScripting.get()) {
            ConfigLibrary.addFile(Constants.StackUpperConfigRuleset, true);
            ConfigLibrary.Start();
        }
        StackSupplier.updateMaxStack();
    }
}
