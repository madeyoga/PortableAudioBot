package guild;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;

public interface IGuildAudioManager {
    GuildAudioState getAudioState(Guild guild);
    Map<String, GuildAudioState> getAudioStates();
    void removeAudioState(Guild guild);
}
