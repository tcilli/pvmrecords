package com.phukka.pvmrecords.leaderboards;

import com.phukka.pvmrecords.Constants;
import com.phukka.pvmrecords.Main;

import java.util.*;
import java.util.stream.IntStream;

import static com.phukka.pvmrecords.Constants.*;

public class LeaderBoard {

    public final int FIRST_PLACE_POINTS = 4;
    public final int SECOND_PLACE_POINTS = 2;
    public final int THIRD_PLACE_POINTS = 1;

    public final Map<String, Player> playerMap = new HashMap<>();
    /**
     * Generates points for player submissions based on PVM (Player vs. Monster) boss records.
     * The method retrieves approved submissions from the database for each boss and processes them
     * to calculate points for various enrage levels, group sizes, and hard modes.
     *
     * The player map is cleared before processing to ensure a fresh calculation.
     *
     * @implNote This method iterates through a predefined list of bosses and considers submissions
     *           for each boss individually. For each boss, it calculates points for different enrage
     *           levels and group sizes, including both normal and hard mode encounters.
     *
     * @implSpec The method relies on the availability of a MySQL database connection through the
     *           `Main.mySQL` instance. Approved submissions are retrieved using the `getApprovedSubmissions`
     *           method and processed to generate points for each combination of boss, enrage level, group size,
     *           and hard mode setting.
     *
     * @see PVMEntry
     * @see Constants#BOSSES
     * @see Constants#ENRAGES
     * @see Constants#GROUP_SIZES
     * @see Main#mySQL
     * @see #handle(List)
     */
    public void generatePoints() {
        playerMap.clear();
        List<PVMEntry> bossRecords = new ArrayList<>();
        Main.mySQL.getApprovedSubmissions(bossRecords);

        for (String boss : BOSSES) {

            List<PVMEntry> entries = bossRecords.stream().filter(entry -> entry.boss().equals(boss)).toList();

            if (entries.isEmpty()) continue;

            for (int enrage : ENRAGES) {
                handle(entries.stream().filter(entry -> entry.enrage() == enrage).toList());
            }
            for (int size : GROUP_SIZES) {
                handle(entries.stream().filter(entry -> entry.groupSize() == size).filter(entry -> !entry.hardMode()).toList());
                handle(entries.stream().filter(entry -> entry.groupSize() == size).filter(PVMEntry::hardMode).toList());
            }
        }
    }

    /**
     * Handles PVM (Player vs. Monster) submissions for a specific boss, considering various factors such as enrages,
     * group sizes, and hardmodes. This method awards points for the first 3 unique submissions.
     *
     * @param submissions A time-ordered List of PVM entries representing player submissions.
     *                    The method processes these entries to award points based on specific criteria.
     *
     * The method ensures uniqueness in submissions by considering the group size and order of usernames.
     * It stops processing after the first 3 unique submissions are identified.
     *
     * The processing involves counting the number of matching usernames between submitted entries
     * and previously processed entries. If the count matches the group size, the groups are considered the same.
     *
     * Points are then awarded to the first 3 unique submissions, and the processing stops.
     */
    public void handle(List<PVMEntry> submissions) {
        if (submissions.size() == 0) return;

        List<PVMEntry> submissionNoRepeats = new ArrayList<>();

        for (PVMEntry record : submissions) {
            // Check if the limit of 3 has been reached
            if (submissionNoRepeats.size() >= 3) {
                break;
            }

            // Counts the number of names that match between the two lists
            // If the number of matches is equal to the group size, then the group is the same but may be ordered differently
            if (submissionNoRepeats.stream().noneMatch(entry -> groupMatches(entry.getUsernames(), record.getUsernames()) == record.groupSize())) {
                submissionNoRepeats.add(record);
            }
        }

        // Process the first 3 entries in submissionNoRepeats
        IntStream.range(0, submissionNoRepeats.size())
            .forEach(index -> handlePoints(submissionNoRepeats.get(index).getUsernames(), getPointsByPlace(index + 1)));
    }

    public void handlePoints(List<String> group, int points) {
        for (String user : group) {
            Player player = playerMap.get(user);
            if (player != null) {
                player.addPoints(points);
            } else {
                playerMap.put(user, new Player(user, points));
            }
        }
    }

    public int getPointsByPlace(int place) {
        return switch (place) {
            case 1 -> FIRST_PLACE_POINTS;
            case 2 -> SECOND_PLACE_POINTS;
            case 3 -> THIRD_PLACE_POINTS;
            default -> 0;
        };
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

    public String getRankings() {
        long start = System.currentTimeMillis();
        generatePoints();
        System.out.println("Generated rankings in " + (System.currentTimeMillis() - start) + "ms");
        List<Player> players = new ArrayList<>(playerMap.values());
        players.sort(Comparator.comparingInt(Player::getPoints).reversed());
        StringBuilder sb = new StringBuilder();
        sb.append("```");
        sb.append("Rankings: 1st place "+ FIRST_PLACE_POINTS +" points, 2nd place "+ SECOND_PLACE_POINTS +" points, 3rd place "+ THIRD_PLACE_POINTS +" point\n\n");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            sb.append("Rank ").append(i + 1).append(": ").append(player.username).append("  - points: ").append(player.points).append("\n");
        }
        sb.append("```");
        return sb.toString();
    }
}
