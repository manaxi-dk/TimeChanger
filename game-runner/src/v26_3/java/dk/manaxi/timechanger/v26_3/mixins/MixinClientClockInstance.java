package dk.manaxi.timechanger.v26_3.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.v26_3.packages.IClientClockInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class MixinClientClockInstance implements IClientClockInstance {
  @Unique
  private boolean timechanger$overworld;

  @Override
  public void timechanger$setOverworld(boolean overworld) {
    this.timechanger$overworld = overworld;
  }

  /** Alter only the displayed time. Vanilla continues to maintain the server clock. */
  @Inject(method = "totalTicks", at = @At("RETURN"), cancellable = true)
  private void timechanger$display(CallbackInfoReturnable<Long> cir) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null) {
      return;
    }
    if (this.timechanger$overworld && addon.configuration().enabled().get()) {
      cir.setReturnValue(addon.getDisplayTime(true));
    }
  }
}
