package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeFormat;
import dk.manaxi.timechanger.core.TimeMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.tooltip.Tooltip;
import net.labymod.api.client.gui.lss.style.modifier.attribute.AttributeState;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.activity.Link;

import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;

import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.configuration.exception.ConfigurationSaveException;

@AutoWidget
@Link("time-picker.lss")
public class TimePickerWidget extends VerticalListWidget<Widget> {
  public enum Section { ALL, MODE, SPEED, SKY }
  private final Section section;
  private static final double[] SPEED_PRESETS = {0.5, 1, 2};
  private static final TimeMode[] MODES = TimeMode.values();
  private static final MathContext SPEED_PRECISION = new MathContext(3);
  private Component speedLabel = Component.empty();
  private long observedMinute = Long.MIN_VALUE;
  private long nextTickLabelUpdate;
  private final TimeChanger addon;
  private final EnumMap<TimeMode, ButtonWidget> modes = new EnumMap<>(TimeMode.class);
  private final List<TimePresetWidget> presets = new ArrayList<>();
  private TextFieldWidget input;
  private SliderWidget speedSlider;
  private ComponentWidget cycleDuration;
  private ComponentWidget speedError;
  private final List<ButtonWidget> speedButtons = new ArrayList<>();
  private double observedSpeed = Double.NaN;
  private TimeTimelineWidget timeline;
  private ComponentWidget readout;
  private ComponentWidget ticks;
  private ComponentWidget error;
  private ComponentWidget status;
  private boolean syncing;
  private boolean dirty;
  private long saveAfter;
  private long observedTime = Long.MIN_VALUE;
  private long observedDisplay = Long.MIN_VALUE;
  private TimeMode observedMode;

  public TimePickerWidget(TimeChanger addon) { this(addon, Section.ALL); }

  public TimePickerWidget(TimeChanger addon, Section section) {
    this.addon = addon;
    this.section = section;
    addId("picker-content");
  }

  private Component text(String key) {
    return Component.translatable("timechanger.picker." + key);
  }

  private ComponentWidget label(String key, String id) {
    ComponentWidget widget = ComponentWidget.component(text(key));
    widget.addId(id);
    return widget;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    modes.clear();
    presets.clear();
    speedButtons.clear();
    observedSpeed = Double.NaN;
    observedMode = null;
    observedTime = Long.MIN_VALUE;
    observedDisplay = Long.MIN_VALUE;
    observedMinute = Long.MIN_VALUE;
    nextTickLabelUpdate = 0;
    VerticalListWidget<Widget> content = this;
    content.addId("picker-content");

    if (section == Section.ALL || section == Section.MODE) initializeMode(content);
    if (section == Section.ALL || section == Section.SPEED) initializeSpeed(content);
    if (section == Section.ALL || section == Section.SKY) initializeSky(content);
    refresh();
  }

  private void initializeMode(VerticalListWidget<Widget> content) {
    HorizontalListWidget modeRow = new HorizontalListWidget();
    modeRow.addId("mode-row");
    for (TimeMode mode : TimeMode.values()) {
      ButtonWidget button = ButtonWidget.component(Component.translatable(mode.translationKey()), () -> {
        if (addon.getMode() == mode) return;
        addon.setMode(mode);
        markDirty();
        refresh();
      });
      button.addId("mode-button");
      button.tooltip(Tooltip.text(Component.translatable(mode.translationKey() + "Description")));
      modes.put(mode, button);
      modeRow.addEntry(button);
    }
    content.addChild(modeRow);
    status = label("serverHint", "picker-hint");
    content.addChild(status);
  }

  private void initializeSpeed(VerticalListWidget<Widget> content) {
    content.addChild(label("cycleSpeed", "picker-hint"));
    HorizontalListWidget speedRow = new HorizontalListWidget();
    speedRow.addId("mode-row");
    for (double speed : SPEED_PRESETS) {
      ButtonWidget button = ButtonWidget.component(Component.text(speed + "×"), () -> {
        if (addon.getMode() != TimeMode.CYCLING) return;
        addon.setCycleSpeed(speed);
        observedSpeed = Double.NaN;
        markDirty();
        refresh();
      });
      button.addId("mode-button");
      speedButtons.add(button);
      speedRow.addEntry(button);
    }
    content.addChild(speedRow);
    speedSlider = new SliderWidget(1, position -> {
      if (syncing || addon.getMode() != TimeMode.CYCLING) return;
      double speed = BigDecimal.valueOf(Math.pow(10, position / 1000.0))
          .round(SPEED_PRECISION).doubleValue();
      addon.setCycleSpeed(Math.max(0.001, Math.min(100, speed)));
      markDirty();
      refresh();
    }).range(-3000, 2000);
    speedSlider.addId("cycle-speed-slider");
    speedSlider.withFormatter(() -> speedLabel);
    content.addChild(speedSlider);
    cycleDuration = ComponentWidget.empty();
    cycleDuration.addId("picker-hint");
    cycleDuration.tooltip(Tooltip.text(text("durationHint")));
    content.addChild(cycleDuration);
    speedError = ComponentWidget.empty();
    speedError.addId("picker-error");
    content.addChild(speedError);
  }

  private void initializeSky(VerticalListWidget<Widget> content) {
    SkyPreviewWidget preview = new SkyPreviewWidget(addon);
    preview.addId("sky-preview");
    content.addChild(preview);
    content.addChild(label("preview", "preview-caption"));

    PresetGridWidget grid = new PresetGridWidget();
    grid.addId("preset-grid");
    long[] times = {0, 6000, 13000, 18000};
    String[] names = {"sunrise", "noon", "sunset", "midnight"};
    for (int i = 0; i < times.length; i++) {
      long time = times[i];
      TimePresetWidget preset = new TimePresetWidget(addon, time, names[i], () -> selectTime(time));
      preset.tooltip(Tooltip.text(Component.text(TimeFormat.clock(time) + " · " + time)));
      presets.add(preset);
      grid.addTile(preset);
    }
    content.addChild(grid);
    timeline = new TimeTimelineWidget(addon, this::selectTime);
    timeline.addId("time-timeline");
    timeline.tooltip(Tooltip.text(text("timelineHint")));
    content.addChild(timeline);
    readout = ComponentWidget.empty();
    readout.addId("time-readout");
    content.addChild(readout);
    ticks = ComponentWidget.empty();
    ticks.addId("tick-readout");
    content.addChild(ticks);
    content.addChild(label("exactTime", "input-caption"));
    input = new TextFieldWidget();
    input.addId("exact-time");
    input.maximalLength(20);
    input.updateListener(value -> {
      if (syncing || addon.getMode() == TimeMode.SERVER) return;
      try {
        long parsed = Long.parseLong(value.trim());
        addon.setTime(parsed);
        observedTime = addon.getTime();
        error.setComponent(Component.empty());
        markDirty();
        refresh();
      } catch (NumberFormatException exception) {
        error.setComponent(text("invalidTime"));
      }
    });
    content.addChild(input);
    error = ComponentWidget.empty();
    error.addId("picker-error");
    content.addChild(error);

  }

  private void selectTime(long time) {
    if (addon.getMode() == TimeMode.SERVER) return;
    addon.setTime(time);
    observedTime = Long.MIN_VALUE;
    error.setComponent(Component.empty());
    markDirty();
    refresh();
  }

  private void markDirty() {
    dirty = true;
    saveAfter = System.nanoTime() + 400_000_000L;
  }

  private Component cycleDuration(double speed) {
    long seconds = Math.round(1200 / speed);
    long[] values = {seconds / 86400, seconds / 3600 % 24, seconds / 60 % 60, seconds % 60};
    String[] units = {"days", "hours", "minutes", "seconds"};
    Component result = Component.empty();
    boolean first = true;
    for (int i = 0; i < values.length; i++) {
      if (values[i] == 0) continue;
      if (!first) result = result.append(Component.space());
      result = result.append(Component.translatable("timechanger.picker.duration." + units[i],
          Component.text(values[i])));
      first = false;
    }
    return result;
  }

  private void refresh() {
    TimeMode mode = addon.getMode();
    long time = addon.getTime();
    long display = addon.getDisplayTime(labyAPI.minecraft().isIngame());
    double speed = addon.getCycleSpeed();
    for (int i = 0; i < speedButtons.size(); i++) {
      boolean selected = mode == TimeMode.CYCLING && Double.compare(speed, SPEED_PRESETS[i]) == 0;
      speedButtons.get(i).setAttributeState(AttributeState.SELECTED, selected);
    }
    if (speedSlider != null && Double.compare(speed, observedSpeed) != 0) {
      syncing = true;
      speedLabel = Component.text(BigDecimal.valueOf(speed).stripTrailingZeros().toPlainString() + "×");
      speedSlider.setValue(Math.log10(speed) * 1000, false);
      cycleDuration.setComponent(Component.translatable("timechanger.picker.cycleDuration", cycleDuration(speed)));
      syncing = false;
      speedError.setComponent(Component.empty());
      observedSpeed = speed;
    }
    if (mode != observedMode) {
      boolean cycling = mode == TimeMode.CYCLING;
      if (speedSlider != null) {
        speedSlider.setActive(cycling);
        for (int i = 0; i < speedButtons.size(); i++) speedButtons.get(i).setEnabled(cycling);
        if (!cycling) {
          speedSlider.setDragging(false);
          speedSlider.setFocused(false);
          speedError.setComponent(Component.empty());
        }
      }
      boolean editable = mode != TimeMode.SERVER;
      if (input != null) {
        input.setEditable(editable);
        input.setActive(editable);
        timeline.setActive(editable);
        if (!editable) {
          timeline.setDragging(false);
          timeline.setFocused(false);
          input.setFocused(false);
          error.setComponent(Component.empty());
        }
      }
      for (int i = 0; i < presets.size(); i++) presets.get(i).setEnabled(editable);
      for (TimeMode candidate : MODES) {
        ButtonWidget button = modes.get(candidate);
        if (button != null) button.setAttributeState(AttributeState.SELECTED, candidate == mode);
      }
      if (status != null) status.setComponent(text(mode == TimeMode.SERVER ? "serverHint"
          : mode == TimeMode.CYCLING ? "cyclingHint" : "fixedHint"));
    }
    if (input != null && time != observedTime) {
      syncing = true;
      input.setText(Long.toString(time));
      syncing = false;
      error.setComponent(Component.empty());
    }
    long minute = ((display + 6000) * 60 / 1000) % 1440;
    if (readout != null && (minute != observedMinute || mode != observedMode)) {
      readout.setComponent(mode == TimeMode.SERVER
          ? Component.translatable("timechanger.picker.savedTime", Component.text(TimeFormat.clock(display)))
          : Component.text(TimeFormat.clock(display)).append(Component.text(" · "))
              .append(Component.translatable(mode.translationKey())));
      observedMinute = minute;
    }
    long now = System.nanoTime();
    if (ticks != null && (time != observedTime || mode != observedMode
        || (display != observedDisplay && now >= nextTickLabelUpdate))) {
      updateTickLabel(display);
      observedDisplay = display;
      nextTickLabelUpdate = now + 250_000_000L;
    }
    observedTime = time;
    observedMode = mode;
  }

  /**
   * The numeric tick readout changes while cycling, so its translated component cannot
   * remain cached indefinitely. Rebuild only on edits or at most four times per second;
   * the clock above is cached until its displayed minute or mode changes.
   */
  private void updateTickLabel(long display) {
    ticks.setComponent(Component.translatable("timechanger.picker.ticks", Component.text(display)));
  }

  @Override
  public void tick() {
    super.tick();
    refresh();
    if (dirty && System.nanoTime() >= saveAfter) save();
  }

  private void save() {
    try {
      addon.saveConfiguration();
      dirty = false;
    } catch (ConfigurationSaveException exception) {
      addon.logger().error("Could not save TimeChanger settings", exception);
      if (error != null) error.setComponent(text("saveFailed"));
      if (speedError != null) speedError.setComponent(text("saveFailed"));
      if (status != null) status.setComponent(text("saveFailed"));
      saveAfter = System.nanoTime() + 5_000_000_000L;
    }
  }

  @Override
  public void dispose() {
    if (dirty) save();
    super.dispose();
  }
}

