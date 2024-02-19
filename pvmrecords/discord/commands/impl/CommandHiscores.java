package com.phukka.pvmrecords.discord.commands.impl;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.Util;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import com.phukka.pvmrecords.discord.commands.ICommand;
import com.phukka.pvmrecords.leaderboards.PVMEntry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.phukka.pvmrecords.Constants.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHiscores implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandHiscores() {
        List<Command.Choice> ENRAGES = Stream.of(ENRAGES_AS_STRING).map(enrage -> new Command.Choice(enrage, enrage)).collect(Collectors.toList());
        this.OPTIONS = List.of(
            new OptionData(OptionType.STRING, BOSSES_1, BOSSES_1_DESC, false).addChoices(BOSSES.stream().limit(25).map(boss -> new Command.Choice(boss, boss)).toList()),
            new OptionData(OptionType.STRING, BOSSES_2, BOSSES_2_DESC, false).addChoices(BOSSES.stream().skip(25).limit(25).map(boss -> new Command.Choice(boss, boss)).toList()),
            new OptionData(OptionType.STRING, USERNAME, USERNAME_DESC, false),
            new OptionData(OptionType.INTEGER, GROUP_SIZE, GROUP_SIZE_DESC, false),
            new OptionData(OptionType.STRING, ENRAGE, ENRAGE_DESC, false).addChoices(ENRAGES),
            new OptionData(OptionType.BOOLEAN, HARDMODE, HARDMODE_DESC, false));
    }

    @Override
    public String getName() {
        return HISCORES_NAME;
    }

    @Override
    public String getDescription() {
        return HISCORES_DESC;
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

        String boss = "";
        String username = "";
        int groupSize = 0;
        int enrage = 0;
        boolean hardMode = false;

        if (event.getOption(BOSSES_1) != null) {
            boss = Objects.requireNonNull(event.getOption(BOSSES_1)).getAsString();
        } else if (event.getOption(BOSSES_2) != null) {
            boss = Objects.requireNonNull(event.getOption(BOSSES_2)).getAsString();
        } else {
            SlashResponseHandler.sendResponse(Main.leaderBoard.getRankings(), event);
            return;
        }
        if (event.getOption(USERNAME) != null) {
            username = Objects.requireNonNull(event.getOption(USERNAME)).getAsString();
            username = username.toLowerCase().trim();
        }
        if (event.getOption(GROUP_SIZE) != null) {
            groupSize = Objects.requireNonNull(event.getOption(GROUP_SIZE)).getAsInt();
        }

        if (event.getOption(ENRAGE) != null) {
            String enrageString = Objects.requireNonNull(event.getOption(ENRAGE)).getAsString();
            // Now, convert the enrage string to an integer
            enrage = Integer.parseInt(enrageString);
        }
        if (event.getOption(HARDMODE) != null) {
            hardMode = Objects.requireNonNull(event.getOption(HARDMODE)).getAsBoolean();
        }

        List<PVMEntry> unfilteredEntries = new ArrayList<>();

        Main.mySQL.getHiscores(boss, username, groupSize, enrage, hardMode, unfilteredEntries);


        List<PVMEntry> filteredEntries = new ArrayList<>(filterRepeatsTop(unfilteredEntries, 30));

        StringBuilder response = new StringBuilder();

        response.append(RESULTS_FOR).append(boss);
        response.append(NEW_LINE);

        if (groupSize == 0) {
            response.append(GROUP_SIZE).append(ANY);
        } else if (groupSize > 0) {
            response.append(GROUP_SIZE).append(SET_AS).append(groupSize).append(" ");
        }
        if (enrage == 0) {
            response.append(ENRAGE).append(ANY);
        } else {
            response.append(ENRAGE).append(SET_AS).append(enrage).append(PERCENT).append(" ");
        }
        if (hardMode) {
            response.append(HARDMODE).append(YES);
        } else {
            response.append(HARDMODE).append(NO);
        }
        response.append(NEW_LINE);

        if (filteredEntries.size() > 0) {
            for (PVMEntry entry : filteredEntries) {
                response.append(Util.convertMessageToLink(Util.convertTicksToString(entry.time()), entry.link())).append(" ").append(entry.usernames());
                if (entry.enrage() > 0) {
                    response.append(" ").append(ENRAGE).append(SET_AS).append(entry.enrage()).append("%");
                }
                response.append(NEW_LINE);
            }
        } else {
            response.append(NO_SUBMISSIONS);
        }
        SlashResponseHandler.sendResponse(response.toString(), event);
    }

    public List<PVMEntry> filterRepeatsTop(List<PVMEntry> submissions, int top) {
        List<PVMEntry> submissionNoRepeats = new ArrayList<>();

        for (PVMEntry record : submissions) {
            // Check if the limit of 3 has been reached
            if (submissionNoRepeats.size() >= top) {
                break;
            }

            // Counts the number of names that match between the two lists
            // If the number of matches is equal to the group size, then the group is the same but may be ordered differently
            if (submissionNoRepeats.stream().noneMatch(entry ->
                groupMatches(entry.getUsernames(), record.getUsernames()) == record.groupSize() &&
                    entry.enrage() == record.enrage())) {
                submissionNoRepeats.add(record);
            }
        }
        return submissionNoRepeats;
    }

    public int groupMatches(List<String> baseGroup, List<String> secondGroup) {
        int matches = 0;
        for (String user : baseGroup) {
            if (secondGroup.contains(user)) {
                matches++;
            }
        }
        return matches;
    }
}
