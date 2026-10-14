package dk.manaxi.timechanger.core;

import dk.manaxi.timechanger.core.ui.TimePickerSettingWidget.TimePickerSetting;
import dk.manaxi.timechanger.core.ui.TimePickerWidget.Section;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequiresExclude;
import net.labymod.api.configuration.settings.annotation.SettingOrder;

@ConfigName("settings")
public class Configuration extends AddonConfig {

  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @TimePickerSetting(Section.MODE)
  @SettingRequiresExclude
  @SettingOrder(0)
  private final ConfigProperty<Boolean> forceDaylightCycle = new ConfigProperty<>(false);
  @TimePickerSetting(Section.SPEED)
  @SettingRequiresExclude
  @SettingOrder(1)
  private final ConfigProperty<Double> cycleSpeed = new ConfigProperty<>(1.0);
  @TimePickerSetting
  @SettingRequiresExclude
  @SettingOrder(2)
  private final ConfigProperty<Long> time = new ConfigProperty<>(0L).addChangeListener(value -> {
    if (TimeChanger.instance == null) return;
    TimeChanger.instance.setDayTime(value);
  });

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> forceDaylightCycle() {
    return forceDaylightCycle;
  }

  public ConfigProperty<Long> time() {
    return time;
  }

  public ConfigProperty<Double> cycleSpeed() {
    return cycleSpeed;
  }

}
