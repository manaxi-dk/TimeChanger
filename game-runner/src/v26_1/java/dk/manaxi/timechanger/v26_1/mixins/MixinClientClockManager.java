package dk.manaxi.timechanger.v26_1.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.v26_1.packages.IClientClockInstance;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.ClockNetworkState;
import net.minecraft.world.clock.WorldClock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;

@Mixin(ClientClockManager.class)
public abstract class MixinClientClockManager {
  @Shadow
  @Final
  private Map<Holder<WorldClock>, ?> clocks;

  @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
  public void calculateManagedRateForClocks(CallbackInfo callbackInfo) {
    if (TimeChanger.instance.configuration().enabled().get()) {
      clocks.forEach((clock, rawInstance) -> {
        IClientClockInstance clockInstance = (IClientClockInstance) rawInstance;
        clockInstance.setClocktime(TimeChanger.instance.configuration().forceDaylightCycle().get() ? TimeChanger.instance.getAndIncrementDayTime() : TimeChanger.instance.getTime());
      });
      callbackInfo.cancel();
    }
  }
}
