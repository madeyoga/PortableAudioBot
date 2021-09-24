import commands.PingCommand;
import commands.SayCommand;
import commands.music.*;
import interactions.ButtonEventHandler;
import exceptions.DuplicateCommandException;
import guild.GuildAudioManager;
import interactions.SlashCommandListener;
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

        SlashCommandListener slashCommandListener = new SlashCommandListener(new ButtonEventHandler());
        slashCommandListener.setPrefix(prefix);
        slashCommandListener.addSlashCommand(new SayCommand());
        slashCommandListener.addSlashCommand(new PingCommand());
        slashCommandListener.addSlashCommand(new JoinCommand());
        slashCommandListener.addSlashCommand(new NowPlayingCommand(audioManager));
        slashCommandListener.addSlashCommand(new PauseCommand(audioManager));
        slashCommandListener.addSlashCommand(new PlayCommand(audioManager));
        slashCommandListener.addSlashCommand(new RepeatModeCommand(audioManager));
        slashCommandListener.addSlashCommand(new ShuffleQueueCommand(audioManager));
        slashCommandListener.addSlashCommand(new SkipCommand(audioManager));
        slashCommandListener.addSlashCommand(new StopCommand(audioManager));
        slashCommandListener.addSlashCommand(new YoutubeSearchCommand(audioManager, searchCommandWaiter));

        builder.addEventListeners(searchCommandWaiter);
        builder.addEventListeners(slashCommandListener);

        // Build JDA
        JDA jda = builder.build();
        slashCommandListener.queueCommands(jda);
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
