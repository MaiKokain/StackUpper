package yuuria.stackupper.stackupper.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpperConfig;
import yuuria.stackupper.stackupper.network.SyncStackSizesPayload;

@EventBusSubscriber(modid = "stackupper")
public class PlayerJoinEvent {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncStackSizesPayload(StackUpperConfig.CONFIG.enableScripting.get(), StackUpperConfig.CONFIG.maxStackGlobally.get(), Constants.generateSyncHashMap()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        Constants.SyncedServerSizes.clear();
    }

    @SubscribeEvent
    public static void onPlayerLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
        Constant.ItemCollection.clear();
    }
}
