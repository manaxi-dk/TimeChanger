package dk.manaxi.timechanger.v1_8_9.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeDataAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldInfo.class)
public class MixinWorldInfo implements TimeDataAccess {
  @Shadow
  private long worldTime;

  @Override
  public long timechanger$getServerTime() {
    return this.worldTime;
  }

  @Inject(method = "getWorldTime", at = @At("RETURN"), cancellable = true)
  private void timechanger$display(CallbackInfoReturnable<Long> cir) {
    TimeChanger addon = TimeChanger.instance;
    if (addon == null || !addon.configuration().enabled().get()) {
      return;
    }
    Minecraft minecraft = Minecraft.getMinecraft();
    if (minecraft != null && minecraft.theWorld != null
        && minecraft.theWorld.getWorldInfo() == (Object) this) {
      cir.setReturnValue(addon.getDisplayTime(true));
    }
  }
}
