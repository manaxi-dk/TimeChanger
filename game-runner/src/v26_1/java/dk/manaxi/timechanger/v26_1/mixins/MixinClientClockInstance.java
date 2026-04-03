package dk.manaxi.timechanger.v26_1.mixins;

import dk.manaxi.timechanger.v26_1.packages.IClientClockInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClockInstance")
public class MixinClientClockInstance implements IClientClockInstance {
  @Shadow
  private long totalTicks;

  @Override
  public void setClocktime(long time) {
    this.totalTicks = time;
  }
}
