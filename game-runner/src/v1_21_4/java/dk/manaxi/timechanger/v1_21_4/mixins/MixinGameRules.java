package dk.manaxi.timechanger.v1_21_4.mixins;

import dk.manaxi.timechanger.core.TimeChanger;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Value;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.class)
public abstract class MixinGameRules {
  @Shadow
  public abstract <T extends Value<T>> T getRule(Key<T> $$0);

  /**
   * @author XXL_Steve
   * @reason Always make daylight cycle game rule return true if option is enabled
   */
  @Overwrite
  public boolean getBoolean(Key<BooleanValue> gameRule) {
    System.out.println("Oh, so it really does check the game rule...");
    if (gameRule.equals(GameRules.RULE_DAYLIGHT) && TimeChanger.instance.configuration().forceDaylightCycle().get() && TimeChanger.instance.configuration().enabled().get()) {
      return true;
    }
    return ((BooleanValue)this.getRule(gameRule)).get();
  }
}