package dk.manaxi.timechanger.core;

import dk.manaxi.timechanger.core.commands.TimeCommand;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class TimeChanger extends LabyAddon<Configuration> {
  public static TimeChanger instance;
  private static long dayTime = 0L; // Used for force daylight cycle

  @Override
  protected void enable() {
    instance = this;

    this.registerSettingCategory();

    this.registerCommand(new TimeCommand(this));

    this.logger().info("Enabled the Addon");
  }

  public long getTime() {
    return configuration().time().get();
  }

  public void setTime(long time) {
    this.setDayTime(time);
    configuration().time().set(time % 24000);
  }

  public long getAndIncrementDayTime() {
    return dayTime++;
  }

  public void setDayTime(long time) {
    dayTime = time;
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
