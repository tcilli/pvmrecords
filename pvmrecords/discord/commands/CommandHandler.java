package com.phukka.pvmrecords.discord.commands;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.discord.RankHandler;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import com.phukka.pvmrecords.discord.commands.impl.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.phukka.pvmrecords.Constants.*;

import java.util.*;

public class CommandHandler extends ListenerAdapter {

    private final Map<String, ICommand> commandMap;

    public CommandHandler() {
        commandMap = new HashMap<>();
        commandMap.put(APPROVE_NAME, new CommandApprove());
        commandMap.put(DECLINE_NAME, new CommandDecline());
        commandMap.put(CHECK_NAME, new CommandCheck());
        commandMap.put(HISCORES_NAME, new CommandHiscores());
        commandMap.put(LOOKUP_NAME, new CommandLookup());
        commandMap.put(SUBMIT_NAME, new CommandSubmit());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Optional.ofNullable(event.getJDA().getGuildById(Main.guildId)).ifPresent(guild -> {
            for (ICommand command : commandMap.values()) {
                Optional.ofNullable(command).ifPresent(cmd -> {
                    guild.upsertCommand(cmd.getName(), cmd.getDescription())
                        .addOptions(cmd.getOptions())
                        .queue();
                });
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Optional.ofNullable(commandMap.get(event.getName())).ifPresent(command -> {
            if (RankHandler.hasPrivilege(event.getMember(), command.getPrivilegeLevel())) {
                System.out.println("Command: " + command.getName() + " executed by " + event.getUser().getName());
                command.execute(event);
                return;
            }
            System.out.println("Command: " + command.getName() + " failed to be executed by " + event.getUser().getName() + " due to insufficient privileges.");
            SlashResponseHandler.sendResponse("You do not have access to this command.", event);
        });
    }
}
