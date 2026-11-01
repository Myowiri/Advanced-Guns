package meow.cats.advancedguns.mixin;

import meow.cats.ClientKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class MixinOptions {

    @Shadow 
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;keyMappings:[Lnet/minecraft/client/KeyMapping;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void addCustomKeybinds(CallbackInfo ci) {
        Options options = (Options) (Object) this;
        this.keyMappings = ArrayUtils.removeElement(this.keyMappings, options.keyTogglePerspective);
        this.keyMappings = ArrayUtils.add(this.keyMappings, ClientKeys.THIRD_PERSON_FRONT);
        this.keyMappings = ArrayUtils.add(this.keyMappings, ClientKeys.SECOND_PERSON_FRONT);
    }

}