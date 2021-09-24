package commands.music;

import interactions.Command;
import guild.GuildAudioManager;
import interactions.CommandCategory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayCommand extends Command {
    GuildAudioManager audioManager;

    public PlayCommand(GuildAudioManager audioManager, CommandCategory category) {
        this.guildOnly = true;
        this.audioManager = audioManager;
        this.commandData = new CommandData("play","Joins your voice channel and starts playing")
                .addOptions(new OptionData(OptionType.STRING,
                    "query","Query can be an url or keywords", true)
                );
        this.category = category;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getMember().getVoiceState() == null) return;

        event.deferReply().queue();

        OptionMapping queryOption = event.getOption("query");
        if (queryOption == null) {
            event.getHook().editOriginal(":x: | Query cannot be empty").queue();
            return;
        }

        String query = queryOption.getAsString();
        audioManager.loadAndPlay(event, query);
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (event.getMember().getVoiceState() == null) return;
        if (arguments.equals("")) {
            event.getChannel().sendMessage(":x: | Query cannot be empty");
        }
        audioManager.loadAndPlay(event, arguments);
    }
}
