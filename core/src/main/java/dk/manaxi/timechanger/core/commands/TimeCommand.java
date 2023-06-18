package dk.manaxi.timechanger.core.commands;

import dk.manaxi.timechanger.core.TimeChanger;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class TimeCommand extends Command {
  public TimeCommand(TimeChanger addon) {
    super("time");

    this.withSubCommand(new TimeSetCommand(addon));
    this.withSubCommand(new TimeAddCommand(addon));
    this.withSubCommand(new TimeResetCommand(addon));
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    displayMessage(Component.translatable("timechanger.commands.help.line1"));
    displayMessage(Component.translatable("timechanger.commands.help.line2"));
    displayMessage(Component.translatable("timechanger.commands.help.line3"));
    return true;
  }
}
