package meow.cats.advancedguns.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CrosshairStatePayload(boolean hideCrosshair) implements CustomPacketPayload {
    public static final Type<CrosshairStatePayload> TYPE = 
        new Type<>(Identifier.fromNamespaceAndPath("weapons", "crosshair_state"));

    public static final StreamCodec<FriendlyByteBuf, CrosshairStatePayload> STREAM_CODEC = 
        StreamCodec.composite(
            ByteBufCodecs.BOOL, CrosshairStatePayload::hideCrosshair,
            CrosshairStatePayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
