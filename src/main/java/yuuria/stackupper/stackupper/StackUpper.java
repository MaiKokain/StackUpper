package yuuria.stackupper.stackupper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import yuuria.stackupper.coremod.Utils;
import yuuria.stackupper.stackupper.events.Client;
import yuuria.stackupper.stackupper.events.ServerStartEvent;

import java.util.Map;

@Mod("stackupper")
public class StackUpper {
    static {
        Utils.setGlobalMaxStackSizeProducer(Constants.globalStackLimitSupplier);
        // Loads special function for different version
        new SpecialSetter();
    }

    public StackUpper(IEventBus modEventBus, ModContainer modContainer)
    {
        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.register(Client.class);
        }
        NeoForge.EVENT_BUS.register(ServerStartEvent.class);
        modContainer.registerConfig(ModConfig.Type.COMMON, StackUpperConfig.CONFIG_SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void checkAndCreateFolder()
    {
        if (!Constants.StackUpperConfigRuleset.exists()) {
            Constants.StackUpperConfigRuleset.mkdir();
        }
    }
}
