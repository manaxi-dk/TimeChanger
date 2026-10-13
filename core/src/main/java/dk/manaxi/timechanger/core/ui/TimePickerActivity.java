package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;

/** Standalone host for the same controls used in the addon settings. */
@AutoActivity
@Link("time-picker.lss")
public class TimePickerActivity extends SimpleActivity {
  private final TimeChanger addon;

  public TimePickerActivity(TimeChanger addon) { this.addon = addon; }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    ScrollWidget scroll = new ScrollWidget(new TimePickerWidget(addon));
    scroll.addId("picker-scroll");
    document().addChild(scroll);
  }
}
