package commands;

import interactions.Command;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.HashMap;
import java.util.Map;

public class SayCommand extends Command {

    Map<String, String[]> signatureCache;

    public SayCommand() {
        this.signatureCache = new HashMap<>();
        this.commandData = new CommandData("say","Makes the bot say what you tell it to")
                .addOptions(new OptionData(OptionType.STRING,
                "content","What the bot should say?", true)
                );
    }

    @Override
    public void execute(SlashCommandEvent event) {
        String content = event.getOption(commandData.getOptions().get(0).getName()).getAsString();
//        event.reply(content).queue();
        String userId = event.getUser().getId();
        String noSignature = provideSignature(userId, "No");
        String yesSignature = provideSignature(userId, "Yes");
        event.reply(content) // prompt the user with a button menu
                .addActionRow(// this means "<style>(<id>, <label>)" the id can be spoofed by the user so setup some kinda verification system
                    Button.secondary(noSignature, "No!"),
                    Button.danger(yesSignature, "Yes!"))
                .queue();

        this.signatureCache.put(event.getUser().getId(), new String[] {noSignature, yesSignature});
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {

    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (!event.isAcknowledged())
            event.deferReply().queue();
        else
            return;
        event.getHook().deleteOriginal().queue();
        String[] id = event.getComponentId().split(":");

        String authorId = id[1];
        String choice = id[2];
        if (this.signatureCache.containsKey(authorId) && authorId.equals(event.getUser().getId())) {
            event.getHook().sendMessage(choice).queue();

            // do something with the choice
            this.signatureCache.remove(authorId);
        }
    }

    @Override
    public String provideSignature(String userId, String choice) {
        return super.provideSignature(userId, choice);
    }
}
