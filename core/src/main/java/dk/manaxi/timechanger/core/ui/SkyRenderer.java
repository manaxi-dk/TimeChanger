package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeFormat;
import net.labymod.api.client.gui.screen.ScreenContext;

/** Small, texture-free pixel landscapes for the picker. */
public final class SkyRenderer {
  private static final long[] TIMES = {0, 3000, 6000, 11000, 13000, 15000, 18000, 23000, 24000};
  private static final int[] SKY = {0xFF947BA7, 0xFF548CC5, 0xFF397EC2, 0xFF6288BC,
      0xFF875F93, 0xFF252C55, 0xFF111D3B, 0xFF3E4267, 0xFF947BA7};
  private static final int[] HORIZON = {0xFFFFC18A, 0xFFBCE2EC, 0xFFA5D9F1, 0xFFFFD49B,
      0xFFEE936A, 0xFF666185, 0xFF354C70, 0xFFD29693, 0xFFFFC18A};

  private SkyRenderer() {}

  public static int sky(long time) { return sample(SKY, time); }
  public static int horizon(long time) { return sample(HORIZON, time); }

  private static int sample(int[] colors, long time) {
    long t = TimeFormat.normalize(time);
    for (int i = 1; i < TIMES.length; i++) {
      if (t <= TIMES[i]) {
        return mix(colors[i - 1], colors[i], (float) (t - TIMES[i - 1]) / (TIMES[i] - TIMES[i - 1]));
      }
    }
    return colors[0];
  }

  public static int mix(int a, int b, float amount) {
    float t = Math.max(0, Math.min(1, amount));
    int r = Math.round(((a >> 16) & 255) * (1 - t) + ((b >> 16) & 255) * t);
    int g = Math.round(((a >> 8) & 255) * (1 - t) + ((b >> 8) & 255) * t);
    int blue = Math.round((a & 255) * (1 - t) + (b & 255) * t);
    return 0xFF000000 | r << 16 | g << 8 | blue;
  }

  public static void rect(ScreenContext context, float x, float y, float w, float h, int color) {
    if (w <= 0 || h <= 0) return;
    context.canvas().submitRelativeRect(x, y, w, h, color);
  }

  public static void celestial(ScreenContext context, float x, float y, float size, boolean night) {
    rect(context, x, y, size, size, night ? 0xFFE8EDF7 : 0xFFFFE7A3);
    if (night) {
      rect(context, x + size * .55F, y + size * .12F, size * .3F, size * .3F, 0xFFBBC8DF);
      rect(context, x + size * .15F, y + size * .6F, size * .25F, size * .2F, 0xFFBBC8DF);
    }
  }

  public static void landscape(ScreenContext context, float x, float y, float w, float h,
      long time, int sky, int horizon) {
    for (int row = 0; row < 24; row++) {
      rect(context, x, y + h * row / 24, w, h / 24 + .2F, mix(sky, horizon, row / 23F));
    }
    boolean night = TimeFormat.isNight(time);
    if (night) {
      for (int i = 0; i < 14; i++) {
        rect(context, x + w * ((i * 37 % 97) / 100F), y + h * ((i * 19 % 43) / 100F), 1, 1, 0xFFBDCBE3);
      }
    }
    double phase = (TimeFormat.normalize(time) % 12000) / 12000.0;
    float sunX = x + w * (.12F + .76F * (float) phase);
    float sunY = y + h * (.55F - .4F * (float) Math.sin(phase * Math.PI));
    celestial(context, sunX, sunY, Math.min(14, h * .17F), night);
    for (int i = 0; i < 16; i++) {
      float hill = .66F + (float) Math.sin(i * .85) * .08F;
      rect(context, x + w * i / 16, y + h * hill, w / 16 + .2F, h * (1 - hill), mix(sky, 0xFF31475A, .7F));
      float ground = .83F + (float) Math.cos(i * .7) * .045F;
      rect(context, x + w * i / 16, y + h * ground, w / 16 + .2F, h * (1 - ground), 0xFF203D3C);
    }
  }
}
