package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.Command;
import interactions.CommandCategory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleQueueCommand extends Command {
    private final GuildAudioManager audioManager;

    public ShuffleQueueCommand(GuildAudioManager audioManager, CommandCategory category) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("shuffle", "Shuffles current queue state");
        this.category = category;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getGuild().getSelfMember().getVoiceState().getChannel() == null) return;
        if (event.getMember().getVoiceState().getChannel() == null) return;
        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        if (audioState.scheduler.getQueue().size() < 1) return;
        shuffleQueue(audioState);
        event.reply(":white_check_mark: Queue shuffled").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (event.getGuild().getSelfMember().getVoiceState().getChannel() == null) return;
        if (event.getMember().getVoiceState().getChannel() == null) return;
        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        if (audioState.scheduler.getQueue().size() < 1) return;
        shuffleQueue(audioState);
        event.getChannel().sendMessage(":white_check_mark: Queue shuffled").queue();
    }

    private void shuffleQueue(GuildAudioState audioState) {
        List<AudioTrack> tracks = new ArrayList<>(audioState.scheduler.getQueue());
        Collections.shuffle(tracks);

        audioState.scheduler.getQueue().clear();
        audioState.scheduler.getQueue().addAll(tracks);
    }
}
