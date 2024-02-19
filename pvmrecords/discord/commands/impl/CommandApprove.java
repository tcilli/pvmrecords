package com.phukka.pvmrecords.discord.commands.impl;

import com.phukka.pvmrecords.Main;
import com.phukka.pvmrecords.discord.response.SlashResponseHandler;
import com.phukka.pvmrecords.discord.commands.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.phukka.pvmrecords.Constants.*;

import java.util.List;
import java.util.Objects;

public class CommandApprove implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandApprove() {
        this.OPTIONS = List.of(
            new OptionData(OptionType.STRING, UID, UID_DESC, true));
    }

    @Override
    public String getName() {
        return APPROVE_NAME;
    }

    @Override
    public String getDescription() {
        return APPROVE_DESC;
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
        if (event.getMember() == null) {
            return;
        }
        if (Main.mySQL.approveSubmission(Objects.requireNonNull(event.getOption(UID)).getAsString())) {
            SlashResponseHandler.sendResponse(SUBMISSION_APPROVED, event);
        } else {
            SlashResponseHandler.sendResponse(SUBMISSION_NOT_FOUND, event);
        }
    }
}
