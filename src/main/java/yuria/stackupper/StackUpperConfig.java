package yuria.stackupper;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = StackUpper.MODID, bus = EventBusSubscriber.Bus.MOD)
public class StackUpperConfig {
    public static final StackUpperConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<Boolean> enableScripting;
    public final ModConfigSpec.ConfigValue<Integer> maxStackGlobally;

    private StackUpperConfig(ModConfigSpec.Builder builder)
    {
        enableScripting = builder
                .comment("Enable scripting")
                .define("stackupper_scripting_feature", true);
        maxStackGlobally = builder
                .comment("Max stack globally")
                .defineInRange("stackupper_max_stack_size", 64, 64, Integer.MAX_VALUE);
    }

    static {
        Pair<StackUpperConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(StackUpperConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @SubscribeEvent
    static void Reloading(ModConfigEvent.Reloading event)
    {
        if (event.getConfig().getModId().equals(StackUpper.MODID) && event.getConfig().getType() == ModConfig.Type.COMMON)
        {
            if (!CONFIG.enableScripting.get() && !StackUpper.itemCollection.cache.isEmpty()) StackUpper.itemCollection.clear();
            if (CONFIG.enableScripting.get()) {
                StackUpper.astProccessor.processFileToArray(StackUpper.StackUpperLangFolder);
                StackUpper.astProccessor.start();
            }
        }
    }
}
