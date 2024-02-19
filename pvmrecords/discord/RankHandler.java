package com.phukka.pvmrecords.discord;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class RankHandler {

    public final static String OWNER = "owner";
    public final static String DEPUTY_OWNER = "deputy owner";
    public final static String OVERSEER = "overseer";
    public final static String COORDINATOR = "coordinator";
    public final static String ORGANISER = "organiser";
    public final static String ADMIN = "admin";

    public static int getPrivileges(@NotNull Member member) {
        switch(member.getRoles().get(0).getName().toLowerCase()) {
            case OWNER          -> { return 0; }
            case DEPUTY_OWNER   -> { return 1; }
            case OVERSEER       -> { return 2; }
            case COORDINATOR    -> { return 3; }
            case ORGANISER      -> { return 4; }
            case ADMIN          -> { return 5; }
            default             -> { return 100; }
        }
    }

    public static boolean hasPrivilege(@NotNull Member member, int privilegeLevel) {
        return getPrivileges(member) <= privilegeLevel;
    }
}
