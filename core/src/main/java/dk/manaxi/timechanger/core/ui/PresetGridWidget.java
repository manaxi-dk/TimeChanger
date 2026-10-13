package dk.manaxi.timechanger.core.ui;

import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.util.bounds.Rectangle;

@AutoWidget
public class PresetGridWidget extends TilesGridWidget<TimePresetWidget> {
  @Override
  public void onBoundsChanged(Rectangle previous, Rectangle next) {
    int columns = next.getWidth() < 360 ? 2 : 4;
    if (tilesPerLine().get() != columns) tilesPerLine().set(columns);
    super.onBoundsChanged(previous, next);
  }
}
