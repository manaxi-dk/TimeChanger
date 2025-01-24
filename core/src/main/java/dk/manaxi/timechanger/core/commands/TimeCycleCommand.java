package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.SubCommand;

public class TimeCycleCommand extends SubCommand {
  private final TimeChanger addon;

  protected TimeCycleCommand(TimeChanger addon) {
    super("cycle", "forcedaylightcycle");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    addon.configuration().forceDaylightCycle().set(!addon.configuration().forceDaylightCycle().get());
    return true;
  }
}
