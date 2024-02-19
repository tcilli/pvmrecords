package com.phukka.pvmrecords.discord.commands.impl;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.Util;
import com.phukka.pvmrecords.discord.RankHandler;
import com.phukka.pvmrecords.discord.commands.ICommand;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.phukka.pvmrecords.Constants.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSubmit implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandSubmit() {
        List<Command.Choice> ENRAGES = Stream.of(ENRAGES_AS_STRING).map(enrage -> new Command.Choice(enrage, enrage)).collect(Collectors.toList());
        this.OPTIONS = List.of(
            new OptionData(OptionType.STRING, TIME, TIME_DESC, true),
            new OptionData(OptionType.STRING, URL, URL_DESC, true),
            new OptionData(OptionType.STRING, USERNAME, ENTER_USERNAME_DESC, true),
            new OptionData(OptionType.STRING, ENRAGE, ENRAGE_DESC, false).addChoices(ENRAGES),
            new OptionData(OptionType.BOOLEAN, HARDMODE, HARDMODE_DESC, false),
            new OptionData(OptionType.STRING, BOSSES_1, BOSSES_1_DESC, false).addChoices(BOSSES.stream().limit(25).map(boss -> new Command.Choice(boss, boss)).toList()),
            new OptionData(OptionType.STRING, BOSSES_2, BOSSES_2_DESC, false).addChoices(BOSSES.stream().skip(25).limit(25).map(boss -> new Command.Choice(boss, boss)).toList())
        );
    }

    @Override
    public String getName() {
        return SUBMIT_NAME;
    }

    @Override
    public String getDescription() {
        return SUBMIT_DESC;
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

        if (event.getOption(BOSSES_1) != null) {
            boss = Objects.requireNonNull(event.getOption(BOSSES_1)).getAsString();
        } else if (event.getOption(BOSSES_2) != null) {
            boss = Objects.requireNonNull(event.getOption(BOSSES_2)).getAsString();
        } else {
            SlashResponseHandler.sendResponse(BOSS_NOT_SELECTED, event);
            return;
        }
        String time = Objects.requireNonNull(event.getOption(TIME)).getAsString();
        String usernames = Objects.requireNonNull(event.getOption(USERNAME)).getAsString();
        int groupSize = Util.getUsernames(usernames).size();
        int enrage = 0;
        if (event.getOption(ENRAGE) != null) {
            String enrageString = Objects.requireNonNull(event.getOption(ENRAGE)).getAsString();
            // Now, convert the enrage string to an integer
            enrage = Integer.parseInt(enrageString);
        }
        boolean hardMode = false;
        if (event.getOption(HARDMODE) != null) {
            hardMode = Objects.requireNonNull(event.getOption(HARDMODE)).getAsBoolean();
        }
        String uid = event.getInteraction().getId();
        if (!Util.isValidTimeFormat(time)) {
            SlashResponseHandler.sendResponse(TIME_DESC, event);
            return;
        }
        int ticks = Util.convertToTicks(time);
        String link = Objects.requireNonNull(event.getOption(URL)).getAsString();

        if (Main.mySQL.submit(uid, boss, ticks, link, usernames, groupSize, enrage, hardMode, AWAITING_APPROVAL)) {

            StringBuilder response = new StringBuilder()
                .append(SUBMISSION_SUCCESS).append(NEW_LINE)
                .append(boss).append(NEW_LINE)
                .append(TIME).append(SET_AS).append(time).append(NEW_LINE)
                .append(USERNAME).append(SET_AS).append(usernames).append(NEW_LINE)
                .append(GROUP_SIZE).append(SET_AS).append(groupSize).append(NEW_LINE)
                .append(ENRAGE).append(SET_AS).append(enrage).append(NEW_LINE)
                .append(HARDMODE).append(SET_AS).append(hardMode).append(NEW_LINE)
                .append(UID).append(SET_AS).append(uid).append(NEW_LINE)
                .append(URL).append(SET_AS).append(link).append(NEW_LINE);

            if (RankHandler.getPrivileges(Objects.requireNonNull(event.getMember())) <= 3) {
                if (Main.mySQL.approveSubmission(uid)) {
                    response.append(SUBMISSION_APPROVED);
                }
            }

            SlashResponseHandler.sendResponse(response.toString(), event);
        } else {
            SlashResponseHandler.sendResponse(SUBMISSION_FAILED, event);
        }
    }
}
