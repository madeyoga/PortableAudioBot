import commands.PingCommand;
import commands.SayCommand;
import commands.music.*;
import interactions.ButtonEventHandler;
import exceptions.DuplicateCommandException;
import guild.GuildAudioManager;
import interactions.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import waiter.SearchCommandResponseWaiter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws LoginException, DuplicateCommandException, FileNotFoundException {
        Scanner scanner = new Scanner(new File("settings.txt"));
        String botToken = scanner.nextLine().split(":", 2)[1].trim();
        String prefix = scanner.nextLine().split(":", 2)[1].trim();
        scanner.close();

        JDABuilder builder = JDABuilder.createDefault(botToken);
        configureMemoryUsage(builder);

        GuildAudioManager audioManager = new GuildAudioManager();
        SearchCommandResponseWaiter searchCommandWaiter = new SearchCommandResponseWaiter(audioManager);

        CommandListener commandListener = new CommandListener(new ButtonEventHandler());
        commandListener.setPrefix(prefix);
        commandListener.addCommand(new SayCommand());
        commandListener.addCommand(new PingCommand());
        commandListener.addCommand(new JoinCommand());
        commandListener.addCommand(new NowPlayingCommand(audioManager));
        commandListener.addCommand(new PauseCommand(audioManager));
        commandListener.addCommand(new PlayCommand(audioManager));
        commandListener.addCommand(new RepeatModeCommand(audioManager));
        commandListener.addCommand(new ShuffleQueueCommand(audioManager));
        commandListener.addCommand(new SkipCommand(audioManager));
        commandListener.addCommand(new StopCommand(audioManager));
        commandListener.addCommand(new YoutubeSearchCommand(audioManager, searchCommandWaiter));

        builder.addEventListeners(searchCommandWaiter);
        builder.addEventListeners(commandListener);

        // Build JDA
        JDA jda = builder.build();
        commandListener.queueCommands(jda);
    }

    private static void configureMemoryUsage(JDABuilder builder) {
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);
        builder.disableCache(CacheFlag.CLIENT_STATUS);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.disableCache(CacheFlag.EMOTE);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Disable presence updates and typing events and more Guild Events
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_EMOJIS);

        builder.disableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }
}
