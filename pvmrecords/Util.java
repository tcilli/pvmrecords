package com.phukka.pvmrecords;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static boolean isValidTimeFormat(String input) {
        // Define the pattern for the time format
        String timeFormatRegex = "^[0-5]\\d:[0-5]\\d\\.\\d$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(timeFormatRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(input);

        // Check if the input string matches the pattern
        return matcher.matches();
    }

    public static int convertToTicks(String timeString) {
        String m = timeString.split(":")[0];
        String s = timeString.split(":")[1].split("\\.")[0];
        String t = timeString.split("\\.")[1];

        if (m == null || s == null || t == null) {
            throw new IllegalArgumentException("Invalid time format. Expected format: minutes:seconds.ticks");
        }

        try {
            int minutes = Integer.parseInt(m);
            int seconds = Integer.parseInt(s);
            double ticks = Double.parseDouble(t);

            // Convert minutes and seconds to seconds, then multiply by 10 to get ticks
            int totalTicks = (int) ((minutes * 60 + seconds + ticks / 10) / 0.6);
            return totalTicks;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric format in time string", e);
        }
    }

    public static String convertTicksToString2(int totalTicks) {
        // Convert totalTicks to seconds
        BigDecimal totalSeconds = BigDecimal.valueOf(totalTicks * 0.6);

        // Calculate minutes, seconds, and remaining ticks
        int minutes = totalSeconds.divide(BigDecimal.valueOf(60), 0, BigDecimal.ROUND_DOWN).intValue();
        int seconds = totalSeconds.remainder(BigDecimal.valueOf(60)).intValue();
        int remainingTicks = totalSeconds.remainder(BigDecimal.valueOf(1)).multiply(BigDecimal.TEN).intValue();

        // Format the result as "mm:ss.t"
        return String.format("%02d:%02d.%d", minutes, seconds, remainingTicks);
    }

    public static String convertTicksToString(int totalTicks) {
        // Convert totalTicks to seconds
        BigDecimal totalSeconds = BigDecimal.valueOf(totalTicks * 0.6);

        // Calculate minutes, seconds, and remaining ticks with proper rounding
        int minutes = totalSeconds.divide(BigDecimal.valueOf(60), 0, RoundingMode.DOWN).intValue();
        int seconds = totalSeconds.remainder(BigDecimal.valueOf(60)).intValue();
        int remainingTicks = totalSeconds.remainder(BigDecimal.valueOf(1))
            .multiply(BigDecimal.TEN)
            .setScale(0, RoundingMode.HALF_UP)
            .intValue();

        // Format the result as "mm:ss.t"
        return String.format("%02d:%02d.%d", minutes, seconds, remainingTicks);
    }


    // Converts a comma-separated list of usernames to a list of usernames
    // Example: "test, test2,test3 ,   test4     , test5" -> ["test", "test2", "test3", "test4", "test5"]
    public static List<String> getUsernames(String usernames) {
        return Arrays.asList(usernames.trim().toLowerCase().split("\\s*,\\s*"));
    }

    public static String convertMessageToLink(String title, String link) {
        return "["+title+"](<"+link+">)";
    }

}
