package dk.manaxi.timechanger.core;

import dk.manaxi.timechanger.core.commands.TimeCommand;
import dk.manaxi.timechanger.core.ui.TimePickerSettingWidget;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class TimeChanger extends LabyAddon<Configuration> {
  public static TimeChanger instance;
  private static double dayTime = 0; // Fractional ticks support slower cycling.
  private static long displayedDayTime = 0L;

  @Override
  protected void enable() {
    instance = this;

    this.labyAPI().widgetRegistry().register(TimePickerSettingWidget.class);
    this.registerSettingCategory();
    setDayTime(getTime());

    this.registerCommand(new TimeCommand(this));

    this.logger().info("Enabled the Addon");
  }

  public long getTime() {
    return TimeFormat.normalize(configuration().time().get());
  }

  public void setTime(long time) {
    this.setDayTime(time);
    configuration().time().set(TimeFormat.normalize(time));
  }

  public long getAndIncrementDayTime() {
    displayedDayTime = (long) dayTime;
    dayTime = (dayTime + getCycleSpeed()) % 24000;
    return displayedDayTime;
  }

  public void setDayTime(long time) {
    dayTime = TimeFormat.normalize(time);
    displayedDayTime = (long) dayTime;
  }

  public double getCycleSpeed() {
    Double speed = configuration().cycleSpeed().get();
    return speed == null || !Double.isFinite(speed) ? 1.0 : Math.max(0.001, Math.min(100, speed));
  }

  public void setCycleSpeed(double speed) {
    if (!Double.isFinite(speed) || speed < 0.001 || speed > 100) {
      throw new IllegalArgumentException("Cycle speed must be between 0.001 and 100");
    }
    configuration().cycleSpeed().set(speed);
  }

  public TimeMode getMode() {
    return TimeMode.from(configuration().enabled().get(), configuration().forceDaylightCycle().get());
  }

  public void setMode(TimeMode mode) {
    if (mode == getMode()) return;
    if (mode == TimeMode.SERVER) {
      configuration().enabled().set(false);
      return;
    }
    configuration().forceDaylightCycle().set(mode == TimeMode.CYCLING);
    setDayTime(getTime());
    configuration().enabled().set(true);
  }

  /** Display reads never advance the counter used by the version-specific time hooks. */
  public long getDisplayTime(boolean worldLoaded) {
    return worldLoaded && getMode() == TimeMode.CYCLING
        ? TimeFormat.normalize(displayedDayTime) : getTime();
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
