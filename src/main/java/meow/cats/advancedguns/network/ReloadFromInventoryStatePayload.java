package meow.cats.advancedguns.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ReloadFromInventoryStatePayload(boolean rfi) implements CustomPacketPayload {
    public static final Type<ReloadFromInventoryStatePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath("weapons", "rfi_state"));

    public static final StreamCodec<FriendlyByteBuf, ReloadFromInventoryStatePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, ReloadFromInventoryStatePayload::rfi,
                    ReloadFromInventoryStatePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

}
