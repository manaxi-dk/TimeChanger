package dk.manaxi.timechanger.v1_21_4.mixins;

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
    System.out.println("get day time    adzadazdzdzaddqsds");
    if(!TimeChanger.instance.configuration().forceDaylightCycle().get() && TimeChanger.instance.configuration().enabled().get()) {
      return TimeChanger.instance.getTime();
    }
    return dayTime;
  }
}