package commands.music;

import interactions.SlashCommand;
import guild.GuildAudioManager;
import guild.GuildAudioState;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class StopCommand extends SlashCommand {
    private final GuildAudioManager audioManager;

    public StopCommand(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.commandData = new CommandData("stop", "Stop playing music and leave voice channel");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        VoiceChannel selfVoiceChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
        if (selfVoiceChannel == null) {
            event.reply(":x: | I'm currently not in a voice channel").queue();
            return;
        }
        event.deferReply().queue();
        event.getGuild().getAudioManager().closeAudioConnection();

        stopAndLeaveVoice(event.getGuild());

        event.getHook().sendMessage("Stopped playing & leave voice channel").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        VoiceChannel selfVoiceChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
        if (selfVoiceChannel == null) {
            event.getChannel().sendMessage(":x: | I'm currently not in a voice channel").queue();
            return;
        }
        event.getGuild().getAudioManager().closeAudioConnection();

        stopAndLeaveVoice(event.getGuild());

        event.getChannel().sendMessage("Stopped playing & left voice channel").queue();
    }

    private void stopAndLeaveVoice(Guild guild) {
        GuildAudioState audioState = audioManager.getAudioState(guild);
        audioState.player.destroy();
        audioState.scheduler.getQueue().clear();
        audioManager.removeAudioState(guild);
    }
}