package yuuria.stackupper.stackupper.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import yuuria.stackupper.stackupper.Constants;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpperConfig;

@EventBusSubscriber(modid = "stackupper")
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToClient(
                SyncStackSizesPayload.TYPE,
                SyncStackSizesPayload.STREAM_CODEC,
                NetworkHandler::handleSyncOnClient
        );
    }

    private static void handleSyncOnClient(final SyncStackSizesPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            StackUpperConfig.ServerConfigCache.CURRENT = new StackUpperConfig.ServerConfigCache(
                    payload.enableScripting(),
                    payload.maxStackGlobally()
            );

            Constants.SyncedServerSizes.clear();
            payload.modifiedSizes().forEach((id, size) -> {
                BuiltInRegistries.ITEM.getOptional(id).ifPresent(item -> {
                    Constants.SyncedServerSizes.put(item, size);
                });
            });
            StackSupplier.updateMaxStack();
        });
    }
}