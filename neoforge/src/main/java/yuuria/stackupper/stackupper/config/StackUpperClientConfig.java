package yuuria.stackupper.stackupper.config;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import yuuria.stackupper.stackupper.StackUpper;

@EventBusSubscriber(modid = StackUpper.MODID)
public class StackUpperClientConfig {
    public static final StackUpperClientConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.BooleanValue enable_debug_commands;

    private StackUpperClientConfig(ModConfigSpec.Builder builder)
    {
        enable_debug_commands = builder
                .translation("Enable debug commands")
                .comment("Enables debugging commands to get info on certain stuff")
                .worldRestart()
                .define("stackupper_client_enable_debug_command", false);
    }

    static {
        Pair<StackUpperClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(StackUpperClientConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }


}
