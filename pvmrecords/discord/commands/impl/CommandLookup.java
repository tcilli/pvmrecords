package com.phukka.pvmrecords.discord.commands.impl;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.Util;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import com.phukka.pvmrecords.discord.commands.ICommand;
import com.phukka.pvmrecords.leaderboards.PVMEntry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.phukka.pvmrecords.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandLookup implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandLookup() {
        this.OPTIONS = List.of(new OptionData(OptionType.STRING, USERNAME, USERNAME_DESC, true));
    }

    @Override
    public String getName() {
        return LOOKUP_NAME;
    }

    @Override
    public String getDescription() {
        return LOOKUP_DESC;
    }

    @Override
    public List<OptionData> getOptions() {
        return OPTIONS;
    }

    @Override
    public int getPrivilegeLevel() {
        return 100;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        SlashResponseHandler.sendResponse(lookup(Objects.requireNonNull(event.getOption(USERNAME)).getAsString()), event);
    }

    public String lookup(String username) {

        username =  username.toLowerCase().trim();

        if (username.length() < 1 || username.length() > 128) {
            return USERNAME_INVALID;
        }
        List<PVMEntry> userSubmissions = new ArrayList<>();

        Main.mySQL.getUserSubmissions(username, userSubmissions);

        StringBuilder message = new StringBuilder();

        message.append(SUBMISSIONS_FOR).append(username).append("\n");

        //loop through bosses
        for (String boss : BOSSES) {

            List<PVMEntry> entries = userSubmissions.stream()
                .filter(entry -> entry.boss().equals(boss))
                .toList();

            if (entries.size() == 0) {
                continue;
            }
            message.append(NEW_LINE).append(boss).append(NEW_LINE);

            for (PVMEntry entry : entries) {
                message.append(Util.convertMessageToLink(Util.convertTicksToString(entry.time()), entry.link()) +" "+ entry.usernames() +" "+ NEW_LINE);
            }
        }
        return message.toString();
    }
}
