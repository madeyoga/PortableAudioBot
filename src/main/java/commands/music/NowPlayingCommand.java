package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class NowPlayingCommand extends SlashCommand {
    final GuildAudioManager audioManager;

    public NowPlayingCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("song", "Shows current playing audio");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
            event.reply(":x: Not playing anything").queue();
            return;
        }

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());

        AudioTrack playingTrack = audioState.player.getPlayingTrack();
        if (playingTrack == null) {
            event.reply(":x: Not playing anything").queue();
            return;
        }

        String songName = playingTrack.getInfo().title;
        event.reply(":musical_note: Now playing: " + songName).queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (event.getGuild().getSelfMember().getVoiceState() == null) {
            event.getChannel().sendMessage(":x: Not playing anything").queue();
            return;
        }
        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        if (audioState.player.getPlayingTrack() == null) {
            event.getChannel().sendMessage(":x: Not playing anything").queue();
            return;
        }
        String songName = audioState.player.getPlayingTrack().getInfo().title;
        event.getChannel().sendMessage(":musical_note: Now playing: " + songName).queue();
    }
}
