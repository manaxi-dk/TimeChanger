package dk.manaxi.timechanger.v26_1.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerClockManager.class)
public abstract class MixinServerClockManager {
  @Shadow
  @Final
  private Map<Holder<WorldClock>, ?> clocks;

  @Shadow
  public abstract void setTotalTicks(Holder<WorldClock> clock, long totalTicks);

  @Inject(method = "tick", at = @At("HEAD"))
  public void calculateManagedRateForClocks(CallbackInfo callbackInfo) {
    if (TimeChanger.instance.configuration().enabled().get()) {
      clocks.forEach((clock, _) -> setTotalTicks(clock, TimeChanger.instance.configuration().forceDaylightCycle().get() ? TimeChanger.instance.getAndIncrementDayTime() : TimeChanger.instance.getTime()));
    }
  }
}
