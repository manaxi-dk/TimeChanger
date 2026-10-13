package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingInfo;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;

@AutoWidget
@SettingWidget
@Link("time-picker.lss")
public class TimePickerSettingWidget extends TimePickerWidget {
  public TimePickerSettingWidget(Section section) {
    super(TimeChanger.instance, section);
  }

  @SettingElement(extended = true)
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface TimePickerSetting {
    Section value() default Section.SKY;
  }

  @SettingFactory
  public static class Factory implements WidgetFactory<TimePickerSetting, TimePickerSettingWidget> {
    @Override
    public Class<?>[] types() { return new Class<?>[]{Boolean.class, Double.class, Long.class}; }

    @Override
    public TimePickerSettingWidget[] create(Setting setting, TimePickerSetting annotation,
        SettingInfo<?> info, SettingAccessor accessor) {
      return new TimePickerSettingWidget[]{new TimePickerSettingWidget(annotation.value())};
    }
  }
}
