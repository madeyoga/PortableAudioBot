package interactions;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand {
    void execute(SlashCommandEvent event);
    void execute(MessageReceivedEvent event, String arguments);
    void onButtonClick(ButtonClickEvent event);
    boolean isGuildOnly();

    /**
     * Signature format must start with command name. for example "%s:%s".format(commandName, userChoice)
     * @param userId
     * @param choice
     * @return signature in string
     */
    String provideSignature(String userId, String choice);
    CommandData getCommandData();
}
