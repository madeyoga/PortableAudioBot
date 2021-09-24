package commands.music;

import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.Command;
import interactions.CommandCategory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class VolumeCommand extends Command {
    private final GuildAudioManager audioManager;

    public VolumeCommand(GuildAudioManager audioManager, CommandCategory category) {
        this.audioManager = audioManager;
        this.commandData = new CommandData("volume", "Check or change audio player volume")
                .addOption(OptionType.NUMBER, "number",
                        "Provide a number to change volume", false);
        this.category = category;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getGuild().getSelfMember().getVoiceState().getChannel() == null) return;
        if (event.getMember().getVoiceState().getChannel() == null) return;
        if (!audioManager.getAudioStates().containsKey(event.getGuild().getId())) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        OptionMapping option = event.getOption("number");
        if (option == null) return;
        String arguments = option.getAsString();
        if (!arguments.equals("") && isValidArgument(arguments)) {
            changeVolume(audioState, Integer.parseInt(arguments));
        }
        event.reply(":speaker: " + audioState.player.getVolume() + "%").queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (event.getGuild().getSelfMember().getVoiceState().getChannel() == null) return;
        if (event.getMember().getVoiceState().getChannel() == null) return;
        if (!audioManager.getAudioStates().containsKey(event.getGuild().getId())) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        if (!arguments.equals("") && isValidArgument(arguments)) {
            changeVolume(audioState, Integer.parseInt(arguments));
        }
        event.getChannel().sendMessage(":speaker: " + audioState.player.getVolume() + "%").queue();
    }

    private void changeVolume(GuildAudioState audioState, int volume) {
        audioState.player.setVolume(volume);
    }

    private boolean isValidArgument(String argument) {
        try {
            Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
