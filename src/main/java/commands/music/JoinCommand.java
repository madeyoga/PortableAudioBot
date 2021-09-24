package commands.music;

import interactions.Command;
import guild.GuildAudioManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class JoinCommand extends Command {

    public JoinCommand() {
        this.guildOnly = true;
        this.commandData = new CommandData("join", "Join author voice channel");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.deferReply().queue();
        Member author = event.getMember();
        if (author.getVoiceState().getChannel() == null) return;
        GuildAudioManager.connectToAuthorVoiceChannel(author);
        event.getHook().sendMessage("Joined " + author.getVoiceState().getChannel().getName()).queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        Member author = event.getMember();
        if (author.getVoiceState().getChannel() == null) return;
        GuildAudioManager.connectToAuthorVoiceChannel(author);
        event.getChannel().sendMessage("Joined " + author.getVoiceState().getChannel().getName()).queue();
    }
}
