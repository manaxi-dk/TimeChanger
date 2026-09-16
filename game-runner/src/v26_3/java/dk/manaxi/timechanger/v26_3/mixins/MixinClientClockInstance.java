package dk.manaxi.timechanger.v26_3.mixins;

import dk.manaxi.timechanger.v26_3.packages.IClientClockInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class MixinClientClockInstance implements IClientClockInstance {
  @Shadow
  private long totalTicks;

  @Override
  public void setClocktime(long time) {
    this.totalTicks = time;
  }
}
