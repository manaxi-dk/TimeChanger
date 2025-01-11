package dk.manaxi.timechanger.v1_8_9.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public class MixinGameRules {
  @Inject(method = "getBoolean", at = @At("HEAD"), cancellable = true)
  private void forceDaylightCycle(String lvt_1_1_, CallbackInfoReturnable<Boolean> cir) {
    if (TimeChanger.instance.configuration().forceDaylightCycle().get() && lvt_1_1_.equals("doDaylightCycle")) {
      cir.setReturnValue(true);
    }
  }
}
