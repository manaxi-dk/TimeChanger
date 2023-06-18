package dk.manaxi.timechanger.core;

import dk.manaxi.timechanger.core.commands.TimeCommand;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class TimeChanger extends LabyAddon<Configuration> {
  public static TimeChanger instance;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    instance = this;

    this.registerCommand(new TimeCommand(this));

    this.logger().info("Enabled the Addon");
  }

  public long getTime() {
    return configuration().time().get();
  }

  public void setTime(long time) {
    configuration().time().set(time % 24000);
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
