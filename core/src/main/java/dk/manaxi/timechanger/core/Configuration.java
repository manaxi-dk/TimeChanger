package dk.manaxi.timechanger.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class Configuration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> forceDaylightCycle = new ConfigProperty<>(false);
  @SliderSetting(steps = 100, min = 0, max = 24000)
  private final ConfigProperty<Long> time = new ConfigProperty<>(0L).addChangeListener(value -> {
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
}