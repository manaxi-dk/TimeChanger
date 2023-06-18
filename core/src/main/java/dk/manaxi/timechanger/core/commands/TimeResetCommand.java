package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.SubCommand;

public class TimeResetCommand extends SubCommand {
  private final TimeChanger addon;

  protected TimeResetCommand(TimeChanger addon) {
    super("reset");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    addon.setTime(-1);
    return true;
  }
}