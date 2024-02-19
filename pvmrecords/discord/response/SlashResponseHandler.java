package com.phukka.pvmrecords.discord.response;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import static com.phukka.pvmrecords.Constants.*;

public class SlashResponseHandler {

    public static void sendResponse(String message, SlashCommandInteractionEvent event) {

        StringBuilder consoleInfo = new StringBuilder();

        consoleInfo.append(MESSAGE_RESPONSE_HEADER).append(event.getInteraction().getId());

        if (message.length() > 6000) {
            event.reply(MESSAGE_RESPONSE_LIMIT_EXCEEDED).queue();
            return;
        }
        if (message.length() <= 2000) {
            event.reply(message)
                .queue(
                    success -> {
                        System.out.println(MESSAGE_RESPONSE_SUCCESS);
                    },
                    failure -> {
                        System.out.println(MESSAGE_RESPONSE_FAILED + event.getInteraction().getId());
                        failure.printStackTrace();
                    }
                );
        } else {
            int page_size = 2000;
            int startIndex = 0;
            int endIndex;

            while (startIndex < message.length()) {
                endIndex = Math.min(startIndex + page_size, message.length());

                // Find the nearest line break or space within the range
                int splitIndex = message.lastIndexOf("\n", endIndex);
                if (splitIndex == -1 || splitIndex < startIndex || splitIndex >= endIndex - 1) {
                    splitIndex = message.lastIndexOf(" ", endIndex);
                    if (splitIndex == -1 || splitIndex < startIndex || splitIndex >= endIndex - 1) {
                        splitIndex = endIndex; // If no suitable breaking point is found, use the end index
                    }
                }
                String part = message.substring(startIndex, splitIndex);

                if (!part.trim().isEmpty()) {
                    if (startIndex == 0) {
                        event.reply(part).queue(
                            success -> {
                                System.out.println(MESSAGE_RESPONSE_SUCCESS);
                            },
                            failure -> {
                                System.out.println(MESSAGE_RESPONSE_FAILED + event.getInteraction().getId());
                                failure.printStackTrace();
                            });
                    } else {
                        event.getChannel().sendMessage(part).queue(
                            success -> {
                                System.out.println(MESSAGE_RESPONSE_SUCCESS);
                            },
                            failure -> {
                                System.out.println(MESSAGE_RESPONSE_FAILED + event.getInteraction().getId());
                                failure.printStackTrace();
                            });
                    }
                }
                // Move the start index to the next part
                startIndex = splitIndex + 1;
            }
        }
        System.out.println(consoleInfo);
    }
}
