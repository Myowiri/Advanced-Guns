package meow.cats.advancedguns.items;

public enum GrenadeType {
    FRAG(60, 0.45f, 0.7f, false),
    MOLOTOV(100, 0.2f, 0.5f, true),
    SMOKE(40, 0.35f, 0.65f, false),
    FLASHBANG(40, 0.5f, 0.75f, false);

    public final int fuseTicks;
    public final float bounciness;
    public final float friction;
    public final boolean breakOnGround;

    GrenadeType(int fuseTicks, float bounciness, float friction, boolean breakOnGround) {
        this.fuseTicks = fuseTicks;
        this.bounciness = bounciness;
        this.friction = friction;
        this.breakOnGround = breakOnGround;
    }
}