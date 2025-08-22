package yuuria.stackupper.stackupper;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import yuuria.stackupper.configlibrary.ConfigLibrary;
import yuuria.stackupper.configlibrary.Constant;

@EventBusSubscriber(modid = "stackupper")
public class StackUpperConfig {
    public static final StackUpperConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.BooleanValue enableScripting;
    public final ModConfigSpec.IntValue maxStackGlobally;

    private StackUpperConfig(ModConfigSpec.Builder builder)
    {
        enableScripting = builder
                .translation("Enable script feature")
                .comment("Enables Scripting feature")
                .define("stackupper_scripting_feature", true);
        maxStackGlobally = builder
                .translation("Global max stack")
                .comment("Number that is returned for ItemStacks that is not in custom ruleset.")
                .comment("NOTE: Items that is equal to 1 will not be returning this value.")
                .defineInRange("stackupper_max_stack", 64, 64, Integer.MAX_VALUE);
    }

    static {
        Pair<StackUpperConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(StackUpperConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @SubscribeEvent
    static void Reloading(ModConfigEvent.Reloading event)
    {
        if (event.getConfig().getModId().equals("stackupper") && event.getConfig().getType() == ModConfig.Type.COMMON) {
            if (CONFIG.enableScripting.get()) {
                if (Constant.FilesArray.isEmpty()) ConfigLibrary.addFile(Constants.StackUpperConfigRuleset, true);
                if (!Constant.ItemCollection.isEmpty()) Constant.ItemCollection.clear();
                ConfigLibrary.Start();
            } else if (!CONFIG.enableScripting.get() && !Constant.ItemCollection.isEmpty()) {
                Constant.FilesArray.clear();
                Constant.ItemCollection.clear();
            }
        }
    }
}
