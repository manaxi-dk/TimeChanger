package dk.manaxi.timechanger.v1_8_9.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldInfo.class)
public class MixinWorldInfo {
  @Shadow
  private long worldTime;

  @Inject(method = "setWorldTime", at = @At("HEAD"), cancellable = true)
  private void setCustomWorldTime(long $$0, CallbackInfo ci) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null) {
      return;
    }
    if (addon.configuration().enabled().get()) {
      if(addon.configuration().forceDaylightCycle().get()) {
        worldTime = addon.getAndIncrementDayTime();
        ci.cancel();
      } else {
        worldTime = addon.getTime();
        ci.cancel();
      }
    }
  }
}
