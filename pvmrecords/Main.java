package com.phukka.pvmrecords;

import com.phukka.pvmrecords.discord.commands.CommandHandler;
import com.phukka.pvmrecords.leaderboards.LeaderBoard;
import com.phukka.pvmrecords.networking.MySQL;
import com.phukka.pvmrecords.content.bingo.networking.MySQLBingo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;

public class Main {

    public static final LeaderBoard leaderBoard = new LeaderBoard();
    public static final BingoLeaderBoard bingoLeaderBoard = new BingoLeaderBoard();
    public static final CommandHandler commandHandler = new CommandHandler();

    public static MySQL mySQL;
    public static Long guildId;

    public static double version = 1.2;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }

        mySQL = new MySQL(args[1], args[2], args[3]);

        guildId = Long.valueOf(args[4]);

        System.out.println("Running version " + version + " of PVMRecords");

        JDA jda = JDABuilder.createLight(args[0], Collections.emptyList())
            .addEventListeners(commandHandler)
            .setActivity(Activity.playing("Type /submit"))
            .build();
    }
}
