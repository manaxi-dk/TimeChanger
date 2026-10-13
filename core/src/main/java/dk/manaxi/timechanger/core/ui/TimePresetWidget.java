package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeMode;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderingOptions;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;

@AutoWidget
public class TimePresetWidget extends ButtonWidget {
  private final TimeChanger addon;
  private final long time;
  private final Component label;

  public TimePresetWidget(TimeChanger addon, long time, String name, Runnable select) {
    super();
    this.label = Component.translatable("timechanger.picker.preset." + name);
    this.addon = addon;
    this.time = time;
    addId("time-preset");
    setPressable(() -> {
      if (addon.getMode() != TimeMode.SERVER) select.run();
    });
  }

  @Override
  public void renderWidget(ScreenContext context) {
    var b = bounds();
    SkyRenderer.landscape(context, b.getX() + 2, b.getY() + 2, b.getWidth() - 4,
        b.getHeight() - 4, time, SkyRenderer.sky(time), SkyRenderer.horizon(time));
    SkyRenderer.rect(context, b.getX() + 2, b.getY() + b.getHeight() - 16,
        b.getWidth() - 4, 14, 0xB0202838);
    if (addon.getMode() != TimeMode.SERVER && addon.getTime() == time) {
      context.canvas().submitRelativeOutlineRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
          2, 0xFF839CFF, 0xFF839CFF);
    }
    float labelWidth = Laby.references().componentRenderer().width(label);
    float scale = Math.min(1, Math.max(1, b.getWidth() - 10) / Math.max(1, labelWidth));
    context.canvas().submitComponent(label, b.getX() + b.getWidth() / 2,
        b.getY() + b.getHeight() - 13, 0xFFFFFFFF, scale,
        TextRenderingOptions.CENTERED | TextRenderingOptions.SHADOW);
    super.renderWidget(context);
  }
}
