package dk.manaxi.timechanger.v1_17_1.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
  @Redirect(method = "tickTime", at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/level/storage/WritableLevelData;getDayTime()J"))
  private long timechanger$serverTime(WritableLevelData data) {
    return ((TimeDataAccess) data).timechanger$getServerTime();
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
