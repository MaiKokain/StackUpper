package yuuria.stackupper.stackupper.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuuria.stackupper.configlibrary.StackUpperConfigLibrary;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.network.SyncStackSizePayload;

@EventBusSubscriber(modid = StackUpper.MODID)
public class StackUpperCommonConfig {
    public static final StackUpperCommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.BooleanValue enable_scripting;
    public final ModConfigSpec.BooleanValue max_stack_stacks_16;
    public final ModConfigSpec.IntValue max_stack_globally;

    private StackUpperCommonConfig(ModConfigSpec.Builder builder)
    {
        enable_scripting = builder
                .translation("Enable script feature")
                .comment("Enables the usage for the scripting feature")
                .define("stackupper_script_feature", true);
        max_stack_globally = builder
                .translation("Global max stack")
                .comment("Returned if scripting feature is disabled or item is not in custom ruleset data (except for item with stack size of 1))")
                .defineInRange("stackupper_max_stack", 64, 64, Integer.MAX_VALUE);
        max_stack_stacks_16 = builder
                .translation("Global stack affects item of 16?")
                .comment("If enabled, items like pearl will get affected by stackupper_max_stack")
                .define("stackupper_max_stack_allows_stacks_of_16", false);
    }

    static {
        Pair<StackUpperCommonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(StackUpperCommonConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @SubscribeEvent
    static void onChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() ==  CONFIG_SPEC) {
            syncAndBroadcast();
            SyncedServerConfig.CURRENT = new SyncedServerConfig(CONFIG.enable_scripting.get(), CONFIG.max_stack_stacks_16.get(), CONFIG.max_stack_globally.getAsInt());
        }
    }

    public static void syncAndBroadcast()
    {
        boolean scriptingEnabled = CONFIG.enable_scripting.getAsBoolean();
        boolean affectsStacksOf16 = CONFIG.max_stack_stacks_16.getAsBoolean();
        int maxStackGlobal = CONFIG.max_stack_globally.getAsInt();

        StackUpper.SyncedServerItems.clear();
        if (scriptingEnabled) {
            StackUpperConfigLibrary.addFile(StackUpper.StackUpperConfigRuleset, true);
            StackUpperConfigLibrary.Start();
        }
        StackSupplier.updateStackLimitFromData();
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            var payload = new SyncStackSizePayload(scriptingEnabled, affectsStacksOf16, maxStackGlobal, StackUpper.generateDataToSync());
            PacketDistributor.sendToAllPlayers(payload);
        }
    }

    public record SyncedServerConfig(boolean enable_script_feature, boolean enable_stacks_of_16, int global_max_stack)
    {
        public static SyncedServerConfig CURRENT = new SyncedServerConfig(true, false, 64);

        @Override
        public boolean enable_script_feature() {
            if (ServerLifecycleHooks.getCurrentServer() != null)
            {
                return StackUpperCommonConfig.CONFIG.enable_scripting.get();
            }
            return this.enable_script_feature;
        }

        @Override
        public boolean enable_stacks_of_16() {
            if (ServerLifecycleHooks.getCurrentServer() != null)
            {
                return StackUpperCommonConfig.CONFIG.max_stack_stacks_16.get();
            }
            return this.enable_stacks_of_16;
        }

        @Override
        public int global_max_stack() {
            if (ServerLifecycleHooks.getCurrentServer() != null)
            {
                return StackUpperCommonConfig.CONFIG.max_stack_globally.getAsInt();
            }
            return this.global_max_stack;
        }
    }
}
