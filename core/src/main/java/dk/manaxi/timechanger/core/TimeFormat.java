package dk.manaxi.timechanger.core;

import java.util.Locale;

public final class TimeFormat {
  public static final long DAY = 24000L;

  private TimeFormat() {}

  public static long normalize(long ticks) {
    return Math.floorMod(ticks, DAY);
  }

  public static String clock(long ticks) {
    long minutes = ((normalize(ticks) + 6000) * 60 / 1000) % 1440;
    return String.format(Locale.ROOT, "%02d:%02d", minutes / 60, minutes % 60);
  }

  public static boolean isNight(long ticks) {
    long time = normalize(ticks);
    return time >= 13000 && time < 23000;
  }
}
