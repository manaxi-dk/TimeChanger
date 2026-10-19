package dk.manaxi.timechanger.v1_19_4.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
  @ModifyExpressionValue(method = "tickTime", at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/level/storage/WritableLevelData;getDayTime()J"))
  private long timechanger$serverTime(long original) {
    return ((TimeDataAccess) ((ClientLevel) (Object) this).getLevelData()).timechanger$getServerTime();
  }

  @Inject(method = "tickTime", at = @At("TAIL"))
  private void timechanger$advance(CallbackInfo ci) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null || !addon.configuration().enabled().get()
        || !addon.configuration().forceDaylightCycle().get()) {
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft != null && minecraft.level == (Object) this) {
      addon.getAndIncrementDayTime();
    }
  }
}
