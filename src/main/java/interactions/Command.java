package interactions;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Map;

public abstract class Command implements ICommand {
    protected boolean guildOnly = true;

    /**
     * Command cooldown in seconds
     */
    protected float cooldown = 0;

    /**
     * User id as key and time in millis as value (Cool down time ends)
     */
    protected Map<String, Long> cooldownMap;
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

    public Map<String, Long> getCooldownMap() {
        return cooldownMap;
    }

    public float getCooldown() {
        return cooldown;
    }
}
