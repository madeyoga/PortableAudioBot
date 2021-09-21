package waiter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Date;
import java.util.List;

public class SearchCommandWaitingState {
    private final List<AudioTrack> choices;
    private final String authorId;
    private final String channelId;
    private final long sentTime;

    public SearchCommandWaitingState(List<AudioTrack> choices, String authorId, String channelId) {
        this.choices = choices;
        this.authorId = authorId;
        this.channelId = channelId;
        this.sentTime = System.currentTimeMillis();
    }

    public List<AudioTrack> getChoices() {
        return choices;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getChannelId() {
        return channelId;
    }

    public long getSentTime() {
        return sentTime;
    }
}
