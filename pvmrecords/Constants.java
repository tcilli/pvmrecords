package com.phukka.pvmrecords;

import java.util.Arrays;
import java.util.List;

/**
 * Constants.java
 * Provides maintainability and readability.
 * <p></p>
 * Memory Efficiency: Declaring strings as final and static can potentially improve memory efficiency.
 * When a string is declared as final and static, it becomes a compile-time constant, and the compiler may optimize its usage.
 *
 */
public class Constants {

    public static final String APPROVE_NAME = "approve";
    public static final String APPROVE_DESC = "approve a submission";

    public static final String DECLINE_NAME = "decline";
    public static final String DECLINE_DESC = "decline a submission";

    public static final String DECLINE_REASON = "reason";
    public static final String DECLINE_REASON_DESC = "describe the reason for declining this submission";

    public static final String CHECK_NAME = "check";
    public static final String CHECK_DESC = "check the status of a submission";

    public static final String LOOKUP_NAME = "lookup";
    public static final String LOOKUP_DESC = "lookup a submission";

    public static final String HISCORES_NAME = "hiscores";
    public static final String HISCORES_DESC = "get the hiscores or view all submissions for specific filters";

    public static final String SUBMIT_NAME = "submit";
    public static final String SUBMIT_DESC = "fill out the fields and any additional information";

    public static final String UID = "uid";
    public static final String UID_DESC = "enter the uid";

    public static final String URL = "url";
    public static final String URL_DESC = "enter an image or video url";

    public static final String TIME = "time";
    public static final String TIME_DESC = "enter the time example: 01:35.4 must match format ##:##.#";

    public static final String BOSS_NOT_SELECTED = "you must select a boss to submit a time for";

    public static final String USERNAME = "username";
    public static final String USERNAME_DESC = "enter the username to lookup";
    public static final String USERNAME_INVALID = "username is invalid";

    public static final String SUBMISSION_APPROVED = "submission approved";
    public static final String SUBMISSION_DECLINED = "submission declined";
    public static final String SUBMISSION_NOT_FOUND = "submission was not found";
    public static final String SUBMISSION_STATUS = "submission status: ";
    public static final String SUBMISSIONS_FOR = "submissions for: ";
    public static final String SUBMISSION_SUCCESS = "submission successful";
    public static final String AWAITING_APPROVAL = "awaiting approval";
    public static final String SUBMISSION_FAILED = "submission failed, An error has occurred";
    public static final String NO_PERMISSION = "you don't have the required permission to use this command";
    public static final String ENTER_USERNAME_DESC = "enter player names separated by commas example: player1, player2";

    public static final String GROUP_SIZE = "group_size";
    public static final String GROUP_SIZE_DESC = "enter amount of people in the group";

    public static final String ENRAGE = "enrage";
    public static final String ENRAGE_DESC = "valid enrages 100, 500, 1000, 2000, 2449, 4000";

    public static final String HARDMODE = "hardmode";
    public static final String HARDMODE_DESC = "select an option";

    public static final String BOSSES_1 = "bosses_abcdefghijklmn";
    public static final String BOSSES_1_DESC = "select a boss from a - n";

    public static final String BOSSES_2 = "bosses_opqrstuvwxyz";
    public static final String BOSSES_2_DESC = "select a boss from o - z";

    public static final String MESSAGE_RESPONSE_DEBUG_LINE = "''''''''''''''''''''''''''''''''''''''";
    public static final String MESSAGE_RESPONSE_HEADER = "Received Interaction ID: ";
    public static final String MESSAGE_RESPONSE_SUCCESS = "Interaction responded successfully";
    public static final String MESSAGE_RESPONSE_FAILED = "Failed to respond to interaction ";
    public static final String MESSAGE_RESPONSE_LIMIT_EXCEEDED = "The response is too long, try narrowing your search";

    public static final String RESULTS_FOR = "Results for ";
    public static final String NO_SUBMISSIONS = "No submissions found";
    public static final String ANY = ": any, ";
    public static final String SET_AS = ": ";
    public static final String PERCENT = "%, ";
    public static final String YES = " yes";
    public static final String NO = " no";
    public static final String NEW_LINE = "\n";

    public static final String[] ENRAGES_AS_STRING = { "100", "500", "1000", "2000", "2449", "4000"};
    public static final int[]              ENRAGES = {  100,   500,   1000,   2000,   2449,   4000 };

    public static final int[] GROUP_SIZES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public static final List<String> BOSSES = Arrays.asList(
        "Araxxor/Araxxi",
        "Arch-Glacor",
        "Ambassador",
        "Astellarn",
        "Corporeal Beast",
        "Crassian Leviathan",
        "Black Stone Dragon",
        "Commander Zilyana (Saradomin's General)",
        "Giant mole",
        "General Graardor (Bandos' General)",
        "Gregorovic",
        "Har-Aken",
        "Hermod",
        "Helwyr",
        "Kalphite Queen",
        "Kalphite King",
        "kerapac",
        "King Black Dragon",
        "K'ril Tsutsaroth (Zamorak's General)",
        "Kree'arra (Armadyl's General)",
        "Legiones",
        "Magister",
        "Masuta the Ascended",
        "Nex",
        "Nex: Angel of Death",
        "Queen Black Dragon",
        "Raksha, the Shadow Colossus",
        "Rasial",
        "Rise of the Six (Barrows Brothers)",
        "Sanctum Guardian",
        "Seiryu the Azure Serpent",
        "Solak",
        "Telos, the Warden",
        "Taraket the Necromancer",
        "Twin Furies (Avaryss and Nymora)",
        "TzTok-Jad",
        "TzKal-Zuk",
        "Vorago",
        "Vindicta & Gorvek",
        "Verak Lith",
        "Zamorak"
    );
}
