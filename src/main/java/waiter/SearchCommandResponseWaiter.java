package waiter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import guild.GuildAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SearchCommandResponseWaiter extends ListenerAdapter {
    private final GuildAudioManager audioManager;
    private final Map<String, SearchCommandWaitingState> waitingUsers;

    public SearchCommandResponseWaiter(GuildAudioManager audioManager) {
        this.audioManager = audioManager;
        this.waitingUsers = new HashMap<>();
    }

    public SearchCommandResponseWaiter(GuildAudioManager audioManager, Map<String,
            SearchCommandWaitingState> waitingUsers) {
        this.audioManager = audioManager;
        this.waitingUsers = waitingUsers;
    }

    public void register(SearchCommandWaitingState state) {
        waitingUsers.put(state.getAuthorId(), state);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (waitingUsers.isEmpty()) return;
        if (event.getAuthor().isBot()) return;
        if (!waitingUsers.containsKey(event.getAuthor().getId())) return;
        if (!isValidArguments(event.getMessage().getContentStripped())) return;

        String authorId = event.getAuthor().getId();
        SearchCommandWaitingState waitingState = waitingUsers.get(authorId);
        if (!waitingState.getChannelId().equals(event.getChannel().getId())) return;
        if (isExpired(waitingState)) {
            waitingUsers.remove(authorId);
            return;
        }

        int selectedIndex = Integer.parseInt(event.getMessage().getContentStripped()) - 1;
        AudioTrack selectedTrack = waitingState.getChoices().get(selectedIndex);
        selectedTrack.setUserData(event.getAuthor().getName());

        GuildAudioManager.play(event.getMember(), audioManager.getAudioState(event.getGuild()), selectedTrack);

        event.getChannel().sendMessage(":musical_note: Added to queue: " + selectedTrack.getInfo().title).queue();

        // finally, remove current author from waiting list
        waitingUsers.remove(authorId);
    }

    private boolean isValidArguments(String arguments) {
        if (arguments == null) return false;
        try {
            int entryNumber = Integer.parseInt(arguments);
            if (entryNumber < 1 || entryNumber > 5) return false;
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    private boolean isExpired(SearchCommandWaitingState state) {
        return ((System.currentTimeMillis() - state.getSentTime()) / 1000) > 60;
    }
}
