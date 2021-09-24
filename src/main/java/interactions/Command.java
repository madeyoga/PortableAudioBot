package interactions;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command implements ICommand {
    protected boolean guildOnly = true;
    protected CommandData commandData;
    protected CommandCategory category;

    public boolean isGuildOnly() {
        return guildOnly;
    }

    @Override
    public String provideSignature(String userId, String choice) {
        return String.format("%s:%s:%s", commandData.getName(), userId, choice);
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) { }

    @Override
    public CommandCategory getCategory() {
        return category;
    }
}
