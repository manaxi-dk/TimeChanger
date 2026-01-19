package dk.manaxi.timechanger.v1_21_11.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
  @Shadow
  private boolean tickDayTime;

  @Inject(method = "setTimeFromServer", at = @At("TAIL"))
  private void setCustomTickDayTime(long time, long timeOfDay, boolean doDaylightCycle, CallbackInfo ci) {
    if (TimeChanger.instance.configuration().forceDaylightCycle().get() && TimeChanger.instance.configuration().enabled().get()) {
      tickDayTime = true;
    }
  }
}
