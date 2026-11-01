package meow.cats.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ReloadPayload() implements CustomPacketPayload {
    public static final Type<ReloadPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("weapons", "reload"));

    public static final StreamCodec<FriendlyByteBuf, ReloadPayload> CODEC = StreamCodec.unit(new ReloadPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
