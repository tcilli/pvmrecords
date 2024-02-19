package com.phukka.pvmrecords.leaderboards;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Player vs. Monster (PVM) submission entry with details such as user ID, boss name,
 * completion time, submission link, usernames, group size, enrage level, hard mode status, approval status,
 * rejection reason, and timestamp.
 *
 * @param uid         The unique identifier for the submission.
 * @param boss        The name of the boss being encountered.
 * @param time        The completion time of the submission.
 * @param link        The link to the submission.
 * @param usernames   A comma-separated string of usernames involved in the submission.
 * @param groupSize   The size of the group participating in the submission.
 * @param enrage      The enrage level of the boss during the submission.
 * @param hardMode    Indicates whether the submission was in hard mode.
 * @param approved    Approval status of the submission (1 for approved, 0 for not approved).
 * @param reason      The reason for rejection if the submission is not approved.
 * @param timestamp   The timestamp indicating when the submission was recorded.
 *
 * @implNote This class uses the record feature in Java for concise representation of immutable data.
 *           It also provides a custom `toString` method for human-readable string representation,
 *           and a `getUsernames` method to obtain a list of usernames from the comma-separated string.
 *
 * @see #toString()
 * @see #getUsernames()
 */
public record PVMEntry(String uid, String boss, int time, String link, String usernames, int groupSize, Integer enrage,
                       Boolean hardMode, int approved, String reason, java.sql.Timestamp timestamp) {

    /**
     * Returns a human-readable string representation of the PVMEntry.
     *
     * @return A string containing details of the PVMEntry.
     */
    @Override
    public String toString() {
        return "PVMEntry{" +
            "uid='" + uid + '\'' +
            ", boss='" + boss + '\'' +
            ", time=" + time +
            ", usernames='" + usernames + '\'' +
            ", groupSize=" + groupSize +
            ", enrage=" + enrage +
            ", hardMode=" + hardMode +
            ", approved=" + approved +
            ", reason='" + reason + '\'' +
            ", timestamp=" + timestamp +
            ", link='" + link + '\'' +
            '}';
    }

    /**
     * Returns a list of usernames from the comma-separated string.
     * The usernames are trimmed and converted to lowercase.
     * @return A list of usernames.
     */
    public List<String> getUsernames() {
        return Arrays.asList(usernames.trim().toLowerCase().split("\\s*,\\s*"));
    }
}


