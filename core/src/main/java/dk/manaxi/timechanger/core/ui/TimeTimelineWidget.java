package dk.manaxi.timechanger.core.ui;

import dk.manaxi.timechanger.core.TimeChanger;
import dk.manaxi.timechanger.core.TimeMode;
import java.util.function.LongConsumer;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.cursor.CursorTypes;

@AutoWidget
public class TimeTimelineWidget extends SimpleWidget {
  private final TimeChanger addon;
  private final LongConsumer select;

  public TimeTimelineWidget(TimeChanger addon, LongConsumer select) {
    this.addon = addon;
    this.select = select;
    // SimpleWidget does not set the ENABLED attribute used by hover cursors.
    setHoverCursor(CursorTypes.RESIZE_EW, true);
  }

  private boolean editable() { return addon.getMode() != TimeMode.SERVER; }

  @Override
  public boolean hasTabFocus() { return editable(); }

  @Override
  public void renderWidget(ScreenContext context) {
    var b = bounds();
    float width = Math.max(1, b.getWidth() - 12);
    float left = b.getX() + 6;
    float top = b.getY() + 6;
    for (int i = 0; i < 96; i++) {
      SkyRenderer.rect(context, left + width * i / 96, top, width / 96 + .2F, 12,
          SkyRenderer.horizon(i * 250L));
    }
    SkyRenderer.celestial(context, left + width * .25F - 3, top + 3, 6, false);
    SkyRenderer.celestial(context, left + width * .75F - 3, top + 3, 6, true);
    float marker = left + width * addon.getDisplayTime(labyAPI.minecraft().isIngame()) / 24000F;
    SkyRenderer.rect(context, marker - 2, top - 3, 4, 18, 0xFF172537);
    SkyRenderer.rect(context, marker - 1, top - 2, 2, 16, 0xFFFFFFFF);
    if (isFocused()) {
      context.canvas().submitRelativeOutlineRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
          1, 0xFFFFD48A, 0xFFFFD48A);
    }
    super.renderWidget(context);
  }

  private void pick(MutableMouse mouse) {
    float fraction = (mouse.getX() - bounds().getX() - 6) / Math.max(1, bounds().getWidth() - 12);
    select.accept(Math.round(Math.max(0, Math.min(1, fraction)) * 240) * 100L);
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton button) {
    if (!editable()) return false;
    if (isHovered() && button == MouseButton.LEFT) {
      setFocused(true);
      setDragging(true);
      pick(mouse);
      return true;
    }
    return super.mouseClicked(mouse, button);
  }

  @Override
  public boolean mouseReleased(MutableMouse mouse, MouseButton button) {
    if (button == MouseButton.LEFT && isDragging()) {
      setDragging(false);
      return true;
    }
    return super.mouseReleased(mouse, button);
  }

  @Override
  public boolean mouseDragged(MutableMouse mouse, MouseButton button, double dx, double dy) {
    if (editable() && isDragging() && button == MouseButton.LEFT) {
      pick(mouse);
      return true;
    }
    return super.mouseDragged(mouse, button, dx, dy);
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if (editable() && isFocused()) {
      if (key == Key.ARROW_LEFT || key == Key.ARROW_RIGHT) {
        select.accept(addon.getTime() + (key == Key.ARROW_LEFT ? -100 : 100));
        return true;
      }
      if (key == Key.HOME || key == Key.END) {
        select.accept(key == Key.HOME ? 0 : 23900);
        return true;
      }
    }
    return super.keyPressed(key, type);
  }
}
