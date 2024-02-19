package com.phukka.pvmrecords.discord.commands.impl;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import com.phukka.pvmrecords.discord.commands.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.phukka.pvmrecords.Constants.*;

import java.util.List;

public class CommandDecline implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandDecline() {
        this.OPTIONS = List.of(
            new OptionData(OptionType.STRING, UID, UID_DESC, true),
            new OptionData(OptionType.STRING, DECLINE_REASON, DECLINE_REASON_DESC, true));
    }

    @Override
    public String getName() {
        return DECLINE_NAME;
    }

    @Override
    public String getDescription() {
        return DECLINE_DESC;
    }

    @Override
    public List<OptionData> getOptions() {
        return OPTIONS;
    }

    @Override
    public int getPrivilegeLevel() {
        return 3;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        SlashResponseHandler.sendResponse(
            Main.mySQL.declineSubmission(event.getOption(UID).getAsString(), event.getOption(DECLINE_REASON).getAsString()) ?
                SUBMISSION_DECLINED : SUBMISSION_NOT_FOUND
            , event);
    }


}
