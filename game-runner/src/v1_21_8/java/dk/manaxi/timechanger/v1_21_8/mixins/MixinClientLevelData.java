package dk.manaxi.timechanger.v1_21_8.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevelData.class)
public class MixinClientLevelData {
  @Shadow
  private long dayTime;

  @Inject(method = "setDayTime", at = @At("HEAD"), cancellable = true)
  private void setCustomDayTime(long $$0, CallbackInfo ci) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null) {
      return;
    }
    if (addon.configuration().enabled().get()) {
      if(addon.configuration().forceDaylightCycle().get()) {
        dayTime = addon.getAndIncrementDayTime();
        ci.cancel();
      } else {
        dayTime = addon.getTime();
        ci.cancel();
      }
    }
  }

}
