package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;

public class TimeAddCommand  extends SubCommand {
  private final TimeChanger addon;

  protected TimeAddCommand(TimeChanger addon) {
    super("add");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(arguments.length == 0) {
      displayMessage(Component.translatable("timechanger.commands.arg1"));
      return true;
    }
    long time = 0;
    try {
      time = Integer.parseInt(arguments[0]);
    } catch (NumberFormatException ignored) {

    }
    addon.setTime(TimeChanger.instance.getTime() + time);
    return true;
  }
}