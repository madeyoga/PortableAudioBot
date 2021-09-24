package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utilities.EventValidator;

public class RepeatModeCommand extends SlashCommand {
    private final GuildAudioManager audioManager;

    public RepeatModeCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("repeat", "Audio track will be added to the queue again after it finished playing");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;
        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());

        boolean repeatMode = !audioState.scheduler.isRepeatMode();
        audioState.scheduler.setRepeatMode(repeatMode);

        if (repeatMode) {
            event.reply(":repeat: AudioTrack will be added to the queue after it finished playing").queue();
        }
        else {
            event.reply(":repeat: Turned off repeat mode").queue();
        }
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;
        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        boolean repeatMode = !audioState.scheduler.isRepeatMode();
        audioState.scheduler.setRepeatMode(repeatMode);

        if (repeatMode)
            event.getChannel()
                .sendMessage(":repeat: Audio track will be added to the queue after it finished playing").queue();
        else
            event.getChannel().sendMessage(":repeat: Turned off repeat mode").queue();
    }
}
