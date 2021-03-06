package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.Command;
import interactions.CommandCategory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utilities.EventValidator;

public class PauseCommand extends Command {
    final private GuildAudioManager audioManager;

    public PauseCommand(GuildAudioManager audioManager, CommandCategory category) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("pause", "Pause or unpause audio player");
        this.category = category;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;
        pause(event.getGuild());
        event.reply(":pause_button: Paused").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (!EventValidator.isVoiceStatesValid(event, audioManager)) return;
        pause(event.getGuild());
        event.getChannel().sendMessage(":pause_button: Paused").queue();
    }

    public void pause(Guild guild) {
        GuildAudioState audioState = audioManager.getAudioState(guild);
        audioState.player.setPaused(!audioState.player.isPaused());
    }
}
