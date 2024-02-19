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

public class CommandCheck implements ICommand {

    private final List<OptionData> OPTIONS;

    public CommandCheck() {
        this.OPTIONS = List.of(new OptionData(OptionType.STRING, UID, UID_DESC, true));
    }

    @Override
    public String getName() {
        return CHECK_NAME;
    }

    @Override
    public String getDescription() {
        return CHECK_DESC;
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
        String status = Main.mySQL.checkApprovalStatus(Objects.requireNonNull(event.getOption(UID)).getAsString());
        StringBuilder response = new StringBuilder();
        if (status == null) {
            response.append(SUBMISSION_NOT_FOUND);
        } else {
            response.append(SUBMISSION_STATUS).append(status);
        }
        SlashResponseHandler.sendResponse(response.toString(), event);
    }
}
