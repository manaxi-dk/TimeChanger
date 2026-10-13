package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;

@AutoWidget
public class SkyPreviewWidget extends SimpleWidget {
  private final TimeChanger addon;
  private int sky;
  private int horizon;
  private long lastFrame;

  public SkyPreviewWidget(TimeChanger addon) {
    this.addon = addon;
    sky = SkyRenderer.sky(addon.getTime());
    horizon = SkyRenderer.horizon(addon.getTime());
  }

  @Override
  public void renderWidget(ScreenContext context) {
    long time = addon.getDisplayTime(labyAPI.minecraft().isIngame());
    long now = System.nanoTime();
    float blend = lastFrame == 0 ? 1 : (float) (1 - Math.exp(-(now - lastFrame) / 120_000_000.0));
    lastFrame = now;
    sky = SkyRenderer.mix(sky, SkyRenderer.sky(time), blend);
    horizon = SkyRenderer.mix(horizon, SkyRenderer.horizon(time), blend);
    var b = bounds();
    SkyRenderer.landscape(context, b.getX(), b.getY(), b.getWidth(), b.getHeight(), time, sky, horizon);
    super.renderWidget(context);
  }
}
