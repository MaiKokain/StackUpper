package yuuria.stackupper.stackupper.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import yuuria.stackupper.stackupper.StackSupplier;
import yuuria.stackupper.stackupper.StackUpper;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;

@EventBusSubscriber(modid = StackUpper.MODID)
public class PayloadHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1.0");
        registrar.playToClient(
                SyncStackSizePayload.TYPE,
                SyncStackSizePayload.STREAM_CODEC,
                PayloadHandler::handleSyncOnClient
        );
    }

    private static void handleSyncOnClient(final SyncStackSizePayload payload, final IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            StackUpperCommonConfig.SyncedServerConfig.CURRENT = new StackUpperCommonConfig.SyncedServerConfig(payload.enable_scripting(), payload.enable_stacks_of_16(), payload.max_stack_global());
            StackUpper.SyncedServerItems.clear();
            payload.modifiedSizes().forEach(((identifier, integer) -> {
                BuiltInRegistries.ITEM.getOptional(identifier).ifPresent(item -> {
                    StackUpper.SyncedServerItems.put(item, integer);
                });
            }));
            StackSupplier.updateStackLimitFromData();
        });
    }
}
