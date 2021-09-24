package commands.music;

import interactions.Command;
import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.CommandCategory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utilities.EventValidator;

public class StopCommand extends Command {
    private final GuildAudioManager audioManager;

    public StopCommand(GuildAudioManager audioManager, CommandCategory category) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("stop", "Stop playing music and leave voice channel");
        this.category = category;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (!EventValidator.isValidAuthorVoice(event)) return;
        VoiceChannel selfVoiceChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
        if (selfVoiceChannel == null) {
            event.reply(":x: | I'm currently not in a voice channel").queue();
            return;
        }

        event.deferReply().queue();

        stopAndLeaveVoice(event.getGuild());

        event.getHook().sendMessage("Stopped playing & leave voice channel").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (!EventValidator.isValidAuthorVoice(event)) return;
        VoiceChannel selfVoiceChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
        if (selfVoiceChannel == null) {
            event.getChannel().sendMessage(":x: | I'm currently not in a voice channel").queue();
            return;
        }

        stopAndLeaveVoice(event.getGuild());

        event.getChannel().sendMessage("Stopped playing & left voice channel").queue();
    }

    private void stopAndLeaveVoice(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
        GuildAudioState audioState = audioManager.getAudioState(guild);
        audioState.player.destroy();
        audioState.scheduler.getQueue().clear();
        audioManager.removeAudioState(guild);
    }
}
