package meow.cats.advancedguns.network;

import io.netty.buffer.ByteBuf;
import meow.cats.Weapons;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ShootPayload() implements CustomPacketPayload {
    public static final Type<ShootPayload> TYPE = new Type<>(Weapons.id("shoot"));
    public static final StreamCodec<ByteBuf, ShootPayload> CODEC = StreamCodec.unit(new ShootPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
