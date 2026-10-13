package dk.manaxi.timechanger.v26_3.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.v26_3.packages.IClientClockInstance;
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
    if (addon.configuration().enabled().get() && addon.configuration().forceDaylightCycle().get()) {
      addon.getAndIncrementDayTime();
    }
  }

  /** Mark the Overworld instance without modifying the manager's private clock map. */
  @Inject(method = "getInstance(Lnet/minecraft/core/Holder;)Lnet/minecraft/client/ClientClockManager$ClientClockInstance;", at = @At("RETURN"))
  private void timechanger$mark(Holder<WorldClock> clock, CallbackInfoReturnable<ClientClockManager.ClientClockInstance> cir) {
    ((IClientClockInstance) cir.getReturnValue()).timechanger$setOverworld(clock.is(WorldClocks.OVERWORLD));
  }
}