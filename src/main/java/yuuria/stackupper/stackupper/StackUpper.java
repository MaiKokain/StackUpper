package yuuria.stackupper.stackupper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import yuuria.stackupper.coremod.Utils;

@Mod("stackupper")

public class StackUpper {
    static {
        Utils.setGlobalMaxStackSizeProducer(StackSupplier.globalStackLimitSupplier);
        // Loads special function for different version
        new SpecialSetter();
    }

    public StackUpper(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON, StackUpperConfig.CONFIG_SPEC);
    }

    public static void checkAndCreateFolder()
    {
        if (!Constants.StackUpperConfigRuleset.exists()) {
            Constants.StackUpperConfigRuleset.mkdir();
        }
    }
}
