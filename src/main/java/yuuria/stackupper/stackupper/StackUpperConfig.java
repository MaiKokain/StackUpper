package yuuria.stackupper.stackupper;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import yuuria.stackupper.configlibrary.ConfigLibrary;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.network.SyncStackSizesPayload;

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
    static void onConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == CONFIG_SPEC) {
            syncAndBroadcastToClients();
            ServerConfigCache.CURRENT = new ServerConfigCache(CONFIG.enableScripting.get(), CONFIG.maxStackGlobally.getAsInt());
        }
    }

    public static void syncAndBroadcastToClients() {
        boolean enabled = CONFIG.enableScripting.get();
        int max = CONFIG.maxStackGlobally.get();

        Constant.ItemCollection.clear();
        if (enabled) {
            ConfigLibrary.addFile(Constants.StackUpperConfigRuleset, true);
            ConfigLibrary.Start();
        }
        StackSupplier.updateMaxStack();

        var server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            var payload = new SyncStackSizesPayload(enabled, max, Constants.generateSyncHashMap());
            PacketDistributor.sendToAllPlayers(payload);
        }
    }

    public record ServerConfigCache(boolean enabled_scripting, int global_max_stack) {
        public static ServerConfigCache CURRENT = new ServerConfigCache(true, 64);

        @Override
        public boolean enabled_scripting() {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                return StackUpperConfig.CONFIG.enableScripting.get();
            }
            return this.enabled_scripting;
        }

        @Override
        public int global_max_stack() {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                return StackUpperConfig.CONFIG.maxStackGlobally.getAsInt();
            }
            return this.global_max_stack;
        }
    }
}
