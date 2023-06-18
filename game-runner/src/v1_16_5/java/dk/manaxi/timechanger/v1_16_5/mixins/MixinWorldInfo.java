package dk.manaxi.timechanger.v1_16_5.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
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
