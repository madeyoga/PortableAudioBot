package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.EventValidator;

public class SkipCommand extends SlashCommand {
    private final GuildAudioManager audioManager;

    public SkipCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        String userId = (String) audioState.player.getPlayingTrack().getUserData();
        if (!userId.equals(event.getUser().getId())) return;

        audioState.scheduler.nextTrack();

        event.reply(":musical_notes: Added to queue: " + audioState.player.getPlayingTrack().getInfo().title)
                .queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        String userId = (String) audioState.player.getPlayingTrack().getUserData();
        if (!userId.equals(event.getAuthor().getId())) return;

        audioState.scheduler.nextTrack();

        event.getChannel().sendMessage(":musical_notes: Added to queue: "
                + audioState.player.getPlayingTrack().getInfo().title).queue();
    }
}
