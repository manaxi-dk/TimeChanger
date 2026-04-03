package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;

public class TimeCycleCommand extends SubCommand {
  private final TimeChanger addon;

  protected TimeCycleCommand(TimeChanger addon) {
    super("cycle", "forcedaylightcycle");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    boolean toggled = !addon.configuration().forceDaylightCycle().get();
    addon.configuration().forceDaylightCycle().set(toggled);
    if (toggled) {
      displayMessage(Component.translatable("timechanger.commands.cycleOn"));
    } else {
      displayMessage(Component.translatable("timechanger.commands.cycleOff"));
    }
    return true;
  }
}
