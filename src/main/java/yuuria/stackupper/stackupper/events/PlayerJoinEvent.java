package yuuria.stackupper.stackupper.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.network.SyncStackSizesPayload;

import java.util.HashMap;

@EventBusSubscriber(modid = "stackupper")
public class PlayerJoinEvent {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            HashMap<ResourceLocation, Integer> sizesToSend = new HashMap<>();

            for (HashMap.Entry<Item, Property> entry : Constant.ItemCollection.entrySet()) {
                Item item = entry.getKey();
                Property prop = entry.getValue();

                int orig = item.getDefaultInstance().getMaxStackSize();
                Constants.logger.info("Calling from player join {} = {}", item, orig);
                long returnedStackSize;

                if (prop.assignOperator != null) {
                    if (prop.assignOperator.name().equals("EQUAL")) {
                        returnedStackSize = prop.assignOperator.apply(prop.assignedBy);
                    } else {
                        returnedStackSize = prop.assignOperator.apply(prop.assignedBy, orig);
                    }
                    int finalSize = (int) Math.min(Math.max(returnedStackSize, 1), Integer.MAX_VALUE);

                    ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                    sizesToSend.put(itemId, finalSize);
                }
            }

            PacketDistributor.sendToPlayer(serverPlayer, new SyncStackSizesPayload(sizesToSend));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        Constants.SyncedServerSizes.clear();
    }
}
