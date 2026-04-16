package yuuria.stackupper.stackupper.events;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import yuuria.stackupper.configlibrary.StackUpperConfigLibrary;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;

@EventBusSubscriber(modid = StackUpper.MODID)
public class ServerStart {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onServerStarted(ServerStartedEvent event)
    {
        if (!StackUpper.StackUpperConfigRuleset.exists()) {
            StackUpper.StackUpperConfigRuleset.mkdir();
        }
        if (StackUpperCommonConfig.CONFIG.enable_scripting.get()) {
            StackUpperConfigLibrary.addFile(StackUpper.StackUpperConfigRuleset, true);
            StackUpperConfigLibrary.Start();
        }
        StackSupplier.updateStackLimitFromData();
    }
}
