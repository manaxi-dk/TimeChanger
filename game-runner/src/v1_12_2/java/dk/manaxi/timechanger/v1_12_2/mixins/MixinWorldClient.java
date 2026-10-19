package dk.manaxi.timechanger.v1_12_2.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {
  @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE",
      target = "Lnet/minecraft/client/multiplayer/WorldClient;getWorldTime()J"))
  private long timechanger$serverTime(long original) {
    return ((TimeDataAccess) ((WorldClient) (Object) this).getWorldInfo()).timechanger$getServerTime();
  }

  @Inject(method = "tick", at = @At("TAIL"))
  private void timechanger$advance(CallbackInfo ci) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null || !addon.configuration().enabled().get()
        || !addon.configuration().forceDaylightCycle().get()) {
      return;
    }
    Minecraft minecraft = Minecraft.getMinecraft();
    if (minecraft != null && minecraft.world == (Object) this) {
      addon.getAndIncrementDayTime();
    }
  }
}
