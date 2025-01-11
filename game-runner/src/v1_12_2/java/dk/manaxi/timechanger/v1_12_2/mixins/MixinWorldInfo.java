package dk.manaxi.timechanger.v1_12_2.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldInfo.class)
public class MixinWorldInfo {
  @Inject(method = "getWorldTime", at = @At("HEAD"), cancellable = true)
  private void returnCustomDayTime(CallbackInfoReturnable<Long> cir) {
    if(!TimeChanger.instance.configuration().forceDaylightCycle().get() && TimeChanger.instance.configuration().enabled().get()) {
      cir.setReturnValue(TimeChanger.instance.getTime());
    }
  }
}