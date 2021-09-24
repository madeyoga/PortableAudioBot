package interactions;

import exceptions.DuplicateCommandException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandListener extends ListenerAdapter {
    protected Map<String, Command> commandMap;
    protected IButtonEventHandler buttonEventHandler;
    protected String prefix = "n.";

    public CommandListener(IButtonEventHandler buttonEventHandler) {
        this.buttonEventHandler = buttonEventHandler;
        this.commandMap = new HashMap<>();
    }

    public CommandListener(IButtonEventHandler buttonEventHandler, Map<String, Command> commandMap) {
        this.buttonEventHandler = buttonEventHandler;
        this.commandMap = commandMap;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().startsWith(prefix)) return;
        if (event.getAuthor().isBot()) return;

        String[] messageContent = event.getMessage().getContentStripped().split(" ", 2);
        String commandName = messageContent[0].substring(prefix.length());
        String arguments = "";

        if (messageContent.length > 1) {
            arguments = messageContent[1];
        }

        Command command = commandMap.getOrDefault(commandName, null);
        if (command != null) {
            if (!event.isFromGuild() && command.isGuildOnly()) return;

            command.execute(event, arguments);
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getUser().isBot()) return;
        Command command = commandMap.getOrDefault(event.getName(), null);
        if (command != null) {
            if (!event.isFromGuild() && command.isGuildOnly()) return;

            command.execute(event);
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String componentId = event.getComponentId();
        if (!buttonEventHandler.verifyComponentId(componentId)) return;
        String commandName = buttonEventHandler.getCommandName(componentId);

        Command command = commandMap.getOrDefault(commandName, null);
        if (command != null) {
            if (!event.isFromGuild() && command.isGuildOnly()) return;
            command.onButtonClick(event);
        }
    }

    public void addCommand(Command command) throws DuplicateCommandException {
        String commandName = command.getCommandData().getName();

        if (commandMap.containsKey(commandName)) {
            throw new DuplicateCommandException("Duplicate command with name '" + commandName + "'");
        }

        commandMap.put(commandName, command);
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public IButtonEventHandler getButtonEventHandler() {
        return buttonEventHandler;
    }

    public void setButtonEventHandler(IButtonEventHandler buttonEventHandler) {
        this.buttonEventHandler = buttonEventHandler;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void queueCommands(JDA jda) {
        CommandListUpdateAction commands = jda.updateCommands();

        Collection<CommandData> commandDataCollection = new ArrayList<>();
        commandMap.values().forEach((item) -> {
            CommandData commandData = item.getCommandData();
            if (commandData == null) return;
            commandDataCollection.add(commandData);
        });

        commands.addCommands(commandDataCollection).queue();
    }
}
