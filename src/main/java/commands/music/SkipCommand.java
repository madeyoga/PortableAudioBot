package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utilities.EventValidator;

public class SkipCommand extends Command {
    private final GuildAudioManager audioManager;

    public SkipCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.commandData = new CommandData("skip", "Skip current playing audio, only author can skip.");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        String userId = (String) audioState.player.getPlayingTrack().getUserData();
        if (!userId.equals(event.getUser().getId())) {
            event.reply(":x: Only current audio requester can skip").queue();
            return;
        }

        audioState.scheduler.nextTrack();
        if (audioState.player.getPlayingTrack() != null)
            event.reply(":musical_note: Now playing: " + audioState.player.getPlayingTrack().getInfo().title)
                    .queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        String userId = (String) audioState.player.getPlayingTrack().getUserData();

        if (!userId.equals(event.getAuthor().getId())) {
            event.getChannel().sendMessage(":x: Only current audio requester can skip").queue();
            return;
        }

        audioState.scheduler.nextTrack();

        if (audioState.player.getPlayingTrack() != null)
            event.getChannel().sendMessage(":musical_note: Now playing: "
                    + audioState.player.getPlayingTrack().getInfo().title).queue();
    }
}
