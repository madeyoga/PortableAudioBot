package commands;

import interactions.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PingCommand extends Command {

    public PingCommand() {
        this.commandData = new CommandData("ping", "Makes the bot says Pong!");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue();
    }

    @Override
    public void execute(MessageReceivedEvent event, String arguments) {
        long time = System.currentTimeMillis();
        event.getChannel().sendMessage(String.format("Pong: %d ms",System.currentTimeMillis() - time)).queue();
    }
}
