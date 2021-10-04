import commands.HelpCommand;
import commands.PingCommand;
import commands.SayCommand;
import commands.music.*;
import exceptions.NullTokenException;
import interactions.ButtonEventHandler;
import exceptions.DuplicateCommandException;
import guild.GuildAudioManager;
import interactions.CommandCategory;
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
    public static void main(String[] args) throws LoginException, DuplicateCommandException, FileNotFoundException,
            NullTokenException {
        Scanner scanner = new Scanner(new File("settings.txt"));

        String botToken = null;
        String prefix = ".";
        String ownerId = null;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(":", 2);
            if (line.length > 1) {
                String key = line[0].trim();
                switch (key) {
                    case "token":
                        botToken = line[1].trim();
                        break;
                    case "prefix":
                        prefix = line[1].trim();
                        break;
                    case "ownerId":
                        ownerId = line[1].trim();
                        break;
                    default:
                        break;
                }
            }
        }

        scanner.close();

        if (botToken == null) {
            throw new NullTokenException("Bot token is not detected. " +
                    "Have you set your bot token in settings.txt? 'token: <yourtokenherexxxxx>'");
        }

        CommandCategory audioCategory = new CommandCategory("Audio");
        CommandCategory generalCategory = new CommandCategory("General");

        JDABuilder builder = JDABuilder.createDefault(botToken);
        configureMemoryUsage(builder);

        GuildAudioManager audioManager = new GuildAudioManager();
        SearchCommandResponseWaiter searchCommandWaiter = new SearchCommandResponseWaiter(audioManager);

        CommandListener commandListener = new CommandListener(new ButtonEventHandler());
        commandListener.setPrefix(prefix);
        commandListener.addCommand(new SayCommand(generalCategory));
        commandListener.addCommand(new PingCommand(generalCategory));
        commandListener.addCommand(new JoinCommand(audioCategory));
        commandListener.addCommand(new NowPlayingCommand(audioManager, audioCategory));
        commandListener.addCommand(new PauseCommand(audioManager, audioCategory));
        commandListener.addCommand(new PlayCommand(audioManager, audioCategory));
        commandListener.addCommand(new RepeatModeCommand(audioManager, audioCategory));
        commandListener.addCommand(new ShuffleQueueCommand(audioManager, audioCategory));
        commandListener.addCommand(new SkipCommand(audioManager, audioCategory));
        commandListener.addCommand(new StopCommand(audioManager, audioCategory));
        commandListener.addCommand(new YoutubeSearchCommand(audioManager, searchCommandWaiter, audioCategory));
        commandListener.addCommand(new HelpCommand(commandListener.getCommandMap()));

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
