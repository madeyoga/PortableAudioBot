package guild;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class GuildAudioManager implements IGuildAudioManager {
    Map<String, GuildAudioState> audioStates;
    private final AudioPlayerManager playerManager;

    private void setupAudioManagers() {
        this.playerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        this.playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public GuildAudioManager() {
        this.audioStates = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        setupAudioManagers();
    }

    public GuildAudioManager(Map<String, GuildAudioState> audioStates) {
        this.audioStates = audioStates;
        this.playerManager = new DefaultAudioPlayerManager();
        setupAudioManagers();
    }

    public void loadAndPlay(final SlashCommandEvent event, String query) {
        if (event.getGuild() == null) return;

        GuildAudioState audioState = getAudioState(event.getGuild());
        Member author = event.getMember();

        boolean isKeywords = false;
        if (!query.startsWith("http")) {
            query = "ytsearch:" + query;
            isKeywords = true;
        }

        final String queryFinal = query;
        final boolean isKeywordsFinal = isKeywords;
        playerManager.loadItemOrdered(audioState, queryFinal, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(author, audioState, track);
                track.setUserData(event.getUser().getName());
                event.getHook().sendMessage(":musical_note: Added to queue: " + track.getInfo().title).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (isKeywordsFinal) {
                    AudioTrack track = playlist.getTracks().remove(0);
                    track.setUserData(event.getUser().getName());
                    play(author, audioState, track);
                    event.getHook().sendMessage(":musical_note: Added to queue: " + track.getInfo().title).queue();
                    return;
                }

                for (AudioTrack track : playlist.getTracks()) {
                    audioState.scheduler.queue(track);
                    track.setUserData(event.getUser().getName());
                }
                String response = String.format(
                        ":musical_note: Added to queue: %s entries from %s", playlist.getTracks().size(), playlist.getName());
                event.getHook().sendMessage(response).queue();
            }

            @Override
            public void noMatches() {
                event.getHook().sendMessage(":x: Nothing found by **" + queryFinal + "**").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getHook().sendMessage(":x: Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void loadAndPlay(final MessageReceivedEvent event, String query) {
        GuildAudioState audioState = getAudioState(event.getGuild());
        Member author = event.getMember();

        boolean isKeywords = false;
        if (!query.startsWith("http")) {
            query = "ytsearch:" + query;
            isKeywords = true;
        }

        final String queryFinal = query;
        final boolean isKeywordsFinal = isKeywords;
        playerManager.loadItemOrdered(audioState, queryFinal, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(author, audioState, track);
                track.setUserData(event.getAuthor().getName());
                event.getChannel().sendMessage(":musical_note: Added to queue: " + track.getInfo().title).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (isKeywordsFinal) {
                    AudioTrack track = playlist.getTracks().remove(0);
                    track.setUserData(event.getAuthor().getName());
                    play(author, audioState, track);
                    event.getChannel().sendMessage(":musical_note: Added to queue: " + track.getInfo().title).queue();
                    return;
                }

                for (AudioTrack track : playlist.getTracks()) {
                    audioState.scheduler.queue(track);
                    track.setUserData(event.getAuthor().getName());
                }
                String response = String.format(
                        ":musical_note: Added to queue: %s entries from %s", playlist.getTracks().size(), playlist.getName());
                event.getChannel().sendMessage(response).queue();
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage(":x: Nothing found by **" + queryFinal + "**").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getChannel().sendMessage(":x: Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public static void connectToAuthorVoiceChannel(Member author) {
        if (author.getVoiceState() == null) return;

        AudioManager audioManager = author.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(author.getVoiceState().getChannel());
        }
    }

    public static void play(Member author, GuildAudioState audioState, AudioTrack track) {
        connectToAuthorVoiceChannel(author);
        audioState.scheduler.queue(track);
    }

    @Override
    public synchronized GuildAudioState getAudioState(Guild guild) {
        String guildId = guild.getId();
        GuildAudioState audioState = audioStates.getOrDefault(guildId, null);

        if (audioState == null) {
            audioState = new GuildAudioState(playerManager);
            audioStates.put(guildId, audioState);
            guild.getAudioManager().setSendingHandler(audioState.getSendHandler());
        }

        return audioState;
    }

    @Override
    public synchronized void removeAudioState(Guild guild) {
        audioStates.remove(guild.getId());
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public Map<String, GuildAudioState> getAudioStates() {
        return audioStates;
    }
}
