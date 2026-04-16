package yuuria.stackupper.stackupper.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;
import yuuria.stackupper.stackupper.network.SyncStackSizePayload;

@EventBusSubscriber(modid = StackUpper.MODID)
public class PlayerSetup {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(
                    player,
                    new SyncStackSizePayload(
                            StackUpperCommonConfig.CONFIG.enable_scripting.get(),
                            StackUpperCommonConfig.CONFIG.max_stack_stacks_16.get(),
                            StackUpperCommonConfig.CONFIG.max_stack_globally.get(),
                            StackUpper.generateDataToSync()
                            )
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingIn event)
    {
        StackUpper.SyncedServerItems.clear();
    }
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingOut event)
    {
        StackUpper.SyncedServerItems.clear();
    }
}
