package guild;

import net.dv8tion.jda.api.entities.Guild;

public interface IGuildAudioManager {
    GuildAudioState getAudioState(Guild guild);
    void removeAudioState(Guild guild);
}
