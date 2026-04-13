package yuuria.stackupper.stackupper.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public record SyncStackSizesPayload(boolean enableScripting, int maxStackGlobally, HashMap<ResourceLocation, Integer> modifiedSizes) implements CustomPacketPayload {
    public static final Type<SyncStackSizesPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("stackupper", "sync_sizes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStackSizesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncStackSizesPayload::enableScripting,
            ByteBufCodecs.INT, SyncStackSizesPayload::maxStackGlobally,
            ByteBufCodecs.map(
                    HashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    ByteBufCodecs.VAR_INT
            ),
            SyncStackSizesPayload::modifiedSizes,
            SyncStackSizesPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}