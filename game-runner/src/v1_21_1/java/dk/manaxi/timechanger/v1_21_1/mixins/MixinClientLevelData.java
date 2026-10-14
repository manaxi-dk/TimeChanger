package dk.manaxi.timechanger.v1_21_1.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevelData.class)
public class MixinClientLevelData implements TimeDataAccess {
  @Shadow
  private long dayTime;

  @Override
  public long timechanger$getServerTime() {
    return this.dayTime;
  }

  @Inject(method = "getDayTime", at = @At("RETURN"), cancellable = true)
  private void timechanger$display(CallbackInfoReturnable<Long> cir) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null || !addon.configuration().enabled().get()) {
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft != null && minecraft.level != null
        && minecraft.level.getLevelData() == (Object) this) {
      cir.setReturnValue(addon.getDisplayTime(true));
    }
  }
}
