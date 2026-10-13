package dk.manaxi.timechanger.core;

import java.util.Locale;

public enum TimeMode {
  SERVER, FIXED, CYCLING;

  public static TimeMode from(boolean enabled, boolean cycling) {
    return !enabled ? SERVER : cycling ? CYCLING : FIXED;
  }

  public String translationKey() {
    return "timechanger.picker.mode." + name().toLowerCase(Locale.ROOT);
  }
}
