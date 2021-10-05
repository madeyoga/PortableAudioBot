package commands;

import interactions.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {
    private final Map<String, Command> commandMap;
    private EmbedBuilder embedBuilder;

    public HelpCommand(Map<String, Command> commandMap) {
        this.commandData = new CommandData("help", "Get command list");
//                .addOption(OptionType.STRING, "command name",
//                        "Use this parameter to get command description", false);
        this.commandMap = commandMap;
        this.addDataToEmbedBuilder();
        this.cooldown = 4;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        OptionMapping option = event.getOption("command name");
        if (option == null) {
            String avaUrl = event.getJDA().getSelfUser().getAvatarUrl();
            this.embedBuilder.setThumbnail(avaUrl);
            event.replyEmbeds(this.embedBuilder.build()).queue();
        }
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (arguments.equals("")) {
            String avaUrl = event.getJDA().getSelfUser().getAvatarUrl();
            this.embedBuilder.setThumbnail(avaUrl);
            event.getChannel().sendMessageEmbeds(this.embedBuilder.build()).queue();
        }
    }

    private void addDataToEmbedBuilder() {
        embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(new Color(100, 100, 255));
        embedBuilder.setFooter(
                "For additional help, feel free to open an issue on Github repo or Join the support server");

        Map <String, String> categoryMap = new HashMap<>();
        for (Command command : commandMap.values()) {
            CommandData data = command.getCommandData();
            String categoryName = command.getCategory().getName();
            if (!categoryMap.containsKey(categoryName)) {
                categoryMap.put(categoryName, "");
            }
            String currentValue = categoryMap.get(categoryName);
            currentValue += String.format("`%s` ", command.getCommandData().getName());
            categoryMap.put(categoryName, currentValue);
        }
        for (String categoryName : categoryMap.keySet()) {
            embedBuilder.addField(categoryName, categoryMap.get(categoryName), false);
        }

        embedBuilder.addField(":tools: Helpful Links",
                "[Report bugs](https://github.com/madeyoga/PortableAudioBot/issues) - [Support Server](https://discord.gg/Y8sB4ay)",
                false);
    }
}
