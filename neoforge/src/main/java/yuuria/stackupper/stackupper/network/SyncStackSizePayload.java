package yuuria.stackupper.stackupper.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

public record SyncStackSizePayload(boolean enable_scripting, boolean enable_stacks_of_16, int max_stack_global, HashMap<Identifier, Integer> modifiedSizes) implements CustomPacketPayload {
    public static final Type<SyncStackSizePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("stackupper", "server_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStackSizePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncStackSizePayload::enable_scripting,
            ByteBufCodecs.BOOL, SyncStackSizePayload::enable_stacks_of_16,
            ByteBufCodecs.INT, SyncStackSizePayload::max_stack_global,
            ByteBufCodecs.map(
                    HashMap::new,
                    Identifier.STREAM_CODEC,
                    ByteBufCodecs.VAR_INT), SyncStackSizePayload::modifiedSizes,
            SyncStackSizePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
