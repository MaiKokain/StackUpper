package yuuria.stackupper.stackupper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import yuuria.stackupper.coremod.Utils;
import yuuria.stackupper.stackupper.events.ServerStartEvent;

@Mod("stackupper")
public class StackUpper {
    static {
        Utils.setGlobalMaxStackSizeProducer(StackSupplier.globalStackLimitSupplier);
        // Loads special function for different version
        new SpecialSetter();
    }

    public StackUpper(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(ServerStartEvent.class);
        modContainer.registerConfig(ModConfig.Type.COMMON, StackUpperConfig.CONFIG_SPEC);
    }

    public static void checkAndCreateFolder()
    {
        if (!Constants.StackUpperConfigRuleset.exists()) {
            Constants.StackUpperConfigRuleset.mkdir();
        }
    }
}
