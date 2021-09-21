package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PauseCommand extends SlashCommand {
    final private GuildAudioManager audioManager;

    public PauseCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("pause", "Pause or unpause audio player");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        pause(event.getGuild());
        event.reply(":pause_button: Paused").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        pause(event.getGuild());
        event.getChannel().sendMessage(":pause_button: Paused").queue();
    }

    public void pause(Guild guild) {
        GuildAudioState audioState = audioManager.getAudioState(guild);
        audioState.player.setPaused(!audioState.player.isPaused());
    }
}
