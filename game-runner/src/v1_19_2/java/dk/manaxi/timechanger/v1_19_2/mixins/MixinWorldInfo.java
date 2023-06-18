package dk.manaxi.timechanger.v1_19_2.mixins;

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
   * @author
   * @reason
   */
  @Overwrite
  public long getDayTime() {
    if(TimeChanger.instance.getTime() != -1) {
      return TimeChanger.instance.getTime();
    }
    return dayTime;
  }
}
