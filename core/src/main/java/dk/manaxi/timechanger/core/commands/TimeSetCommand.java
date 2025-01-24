package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;

public class TimeSetCommand extends SubCommand {
  private final TimeChanger addon;

  protected TimeSetCommand(TimeChanger addon) {
    super("set");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(arguments.length == 0) {
      displayMessage(Component.translatable("timechanger.commands.arg1"));
      return true;
    }
    int time = 0;
    switch (arguments[0]) {
      case "day": {
        time = 6000;
        break;
      }
      case "night": {
        time = 18000;
        break;
      }
      case "sunset": {
        time = 13000;
        break;
      }
      default: {
        try {
          time = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException ignored) {

        }
      }
    }
    addon.setTime(time);
    return true;
  }
}
