package commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import guild.GuildAudioManager;
import guild.GuildAudioState;
import interactions.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import utilities.TimeFormatter;
import waiter.SearchCommandResponseWaiter;
import waiter.SearchCommandWaitingState;

import java.util.ArrayList;
import java.util.List;

public class YoutubeSearchCommand extends SlashCommand {
    private final GuildAudioManager audioManager;
    private final SearchCommandResponseWaiter waiter;

    public YoutubeSearchCommand(GuildAudioManager audioManager, SearchCommandResponseWaiter waiter) {
        this.audioManager = audioManager;
        this.guildOnly = true;
        this.waiter = waiter;
        this.commandData = new CommandData("search", "Search by query from youtube")
                .addOption(OptionType.STRING, "query", "a keywords", true);
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getMember().getVoiceState().getChannel() == null) return;

        event.deferReply().queue();

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        final String arguments = event.getOption("query").getAsString();
        audioManager.getPlayerManager().loadItemOrdered(audioState, "ytsearch:" + arguments,
                new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                List<AudioTrack> tracks = new ArrayList<>();
                tracks.add(track);
                waiter.register(new SearchCommandWaitingState(tracks, event.getUser().getId(),
                        event.getChannel().getId()));

                String response = String.format(
                        "Search result for: %s\n\n1. %s [%s]\n\n_Respond the entry number to start playing_",
                        arguments, track.getInfo().title, TimeFormatter.getDurationFormat(track.getDuration()));
                event.getHook().sendMessage(response).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                waiter.register(new SearchCommandWaitingState(tracks,
                        event.getUser().getId(), event.getChannel().getId()));
                StringBuilder builder = new StringBuilder();
                builder.append("Search result for: ").append(arguments).append("\n");
                for (int i = 0; i < playlist.getTracks().size(); i++) {
                    AudioTrack track = tracks.get(i);
                    String row = String.format("\n%d. **%s** [%s]", i + 1, track.getInfo().title,
                            TimeFormatter.getDurationFormat(track.getDuration()));
                    builder.append(row);
                    if (i == 4) break;
                }
                builder.append("\n\n_Respond the entry number to start playing_");
                event.getHook().sendMessage(builder.toString()).queue();
            }

            @Override
            public void noMatches() {
                event.getHook().sendMessage(":x: Nothing found by **" + arguments + "**").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getHook().sendMessage(":x: Could not load: " + exception.getMessage()).queue();
            }
        });
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        if (event.getMember().getVoiceState() == null) return;

        GuildAudioState audioState = audioManager.getAudioState(event.getGuild());
        audioManager.getPlayerManager().loadItemOrdered(audioState, "ytsearch:" + arguments,
                new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                List<AudioTrack> tracks = new ArrayList<>();
                tracks.add(track);
                waiter.register(new SearchCommandWaitingState(tracks,
                        event.getAuthor().getId(), event.getChannel().getId()));

                String response = String.format(
                        "Search result for: %s\n\n1. %s [%s]\n\n_Respond the entry number to start playing_",
                        arguments, track.getInfo().title, TimeFormatter.getDurationFormat(track.getDuration()));
                event.getChannel().sendMessage(response).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                waiter.register(new SearchCommandWaitingState(tracks,
                        event.getAuthor().getId(), event.getChannel().getId()));
                StringBuilder builder = new StringBuilder();
                builder.append("Search result for: ").append(arguments).append("\n");
                for (int i = 0; i < playlist.getTracks().size(); i++) {
                    AudioTrack track = tracks.get(i);
                    String row = String.format("\n%d. **%s** [%s]", i + 1, track.getInfo().title,
                            TimeFormatter.getDurationFormat(track.getDuration()));
                    builder.append(row);
                    if (i == 4) break;
                }
                builder.append("\n\n_Respond the entry number to start playing_");
                event.getChannel().sendMessage(builder.toString()).queue();
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage(":x: Nothing found by **" + arguments + "**").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getChannel().sendMessage(":x: Could not load: " + exception.getMessage()).queue();
            }
        });
    }
}
