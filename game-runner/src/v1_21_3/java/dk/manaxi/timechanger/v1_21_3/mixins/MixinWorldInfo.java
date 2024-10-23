package dk.manaxi.timechanger.v1_21_3.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevelData.class)
public class MixinWorldInfo {
  @Shadow
  private long dayTime;
  /**
   * @author Itz_Hoffe
   * @reason Set the time to the custom time if enabled
   */
  @Overwrite
  public long getDayTime() {
    if(TimeChanger.instance.configuration().customTime().get() && TimeChanger.instance.configuration().enabled().get()) {
      return TimeChanger.instance.getTime();
    }
    return dayTime;
  }
}