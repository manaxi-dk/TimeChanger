package dk.manaxi.timechanger.v1_12_2.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldInfo.class)
public class MixinWorldInfo {
  @Shadow
  private long worldTime;

  /**
   * @author Itz_Hoffe
   * @reason Set the time to the custom time if enabled
   */
  @Overwrite
  public long getWorldTime() {
    if(TimeChanger.instance.configuration().customTime().get() && TimeChanger.instance.configuration().enabled().get()) {
      return TimeChanger.instance.getTime();
    }
    return worldTime;
  }
}
