package dk.manaxi.timechanger.v26_1.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public abstract class MixinClientClockManager {
  /** Keep Minecraft's clock updates intact; advance the visual override once per update. */
  @Inject(method = "tick", at = @At("TAIL"))
  private void timechanger$advance(long gameTime, CallbackInfo ci) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null) {
      return;
    }
    if (addon.configuration().enabled().get() && addon.configuration().forceDaylightCycle().get()) {
      addon.getAndIncrementDayTime();
    }
  }

  /** Override only the displayed Overworld time; server clocks remain untouched. */
  @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
  private void timechanger$display(Holder<WorldClock> clock, CallbackInfoReturnable<Long> cir) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null) {
      return;
    }
    if (clock.is(WorldClocks.OVERWORLD) && addon.configuration().enabled().get()) {
      cir.setReturnValue(addon.getDisplayTime(true));
    }
  }
}