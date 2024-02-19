package com.phukka.pvmrecords.networking;

import com.phukka.pvmrecords.leaderboards.PVMEntry;

import java.sql.*;
import java.util.List;

public class MySQL {

    protected String URL;
    protected String USER;
    protected String PASSWORD;

    public MySQL(String URL, String USER, String PASSWORD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

    }

    protected java.sql.Connection getConnection() throws SQLException {
        System.out.println("MySQL connection requested");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //queries
    protected final String NEW_SUBMISSION = "INSERT INTO BossFight (uid, boss, time, link, usernames, groupsize, enrage, hardmode, approved, reason, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //modifiers
    protected final String GET_SUBMISSION_APPROVAL_STATUS = "SELECT reason FROM bossfight WHERE uid = ?";
    protected final String SET_SUBMISSION_APPROVAL_STATUS = "UPDATE bossfight SET approved = ?, reason = ? WHERE uid = ?";
    protected final String GET_APPROVED_SUBMISSIONS = "SELECT * FROM bossfight WHERE approved = 1 ORDER BY time ASC, timestamp DESC";
    protected final String GET_SUBMISSIONS_FOR_USERNAME = "SELECT * FROM bossfight WHERE usernames LIKE ? AND approved = 1 ORDER BY timestamp DESC";
    protected final String GET_HISCORES_TIME_ASC = "SELECT * FROM bossfight WHERE boss = ? AND approved = 1 ORDER BY time ASC";


    //Table Structure
    protected final String UID = "uid";
    protected final String BOSS = "boss";
    protected final String TIME = "time";
    protected final String LINK = "link";
    protected final String USERNAMES = "usernames";
    protected final String GROUPSIZE = "groupsize";
    protected final String ENRAGE = "enrage";
    protected final String HARDMODE = "hardmode";
    protected final String APPROVED = "approved";
    protected final String REASON = "reason";
    protected final String TIMESTAMP = "timestamp";


    public final String checkApprovalStatus(String uid) {
        try (PreparedStatement statement = getConnection().prepareStatement(GET_SUBMISSION_APPROVAL_STATUS)) {
            statement.setString(1, uid.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(REASON);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final boolean approveSubmission(String uid) {
        try (PreparedStatement statement = getConnection().prepareStatement(SET_SUBMISSION_APPROVAL_STATUS)) {
            statement.setInt(1, 1);
            statement.setString(2, APPROVED);
            statement.setString(3, uid.trim());
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final boolean declineSubmission(String uid, String reason) {
        try (PreparedStatement statement = getConnection().prepareStatement(SET_SUBMISSION_APPROVAL_STATUS)) {
            statement.setInt(1, 2);
            statement.setString(2, reason.toLowerCase().trim());
            statement.setString(3, uid.trim());
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final boolean submit(String uid, String boss, int time, String link, String usernames, int groupSize, Integer enrage, Boolean hardMode, String message) {
        try (PreparedStatement statement = getConnection().prepareStatement(NEW_SUBMISSION)) {
            statement.setString(1, uid.trim());
            statement.setString(2, boss);
            statement.setInt(3, time);
            statement.setString(4, link);
            statement.setString(5, usernames.toLowerCase().trim());
            statement.setInt(6, groupSize);
            statement.setObject(7, enrage); // Use setObject to handle null values
            statement.setBoolean(8, hardMode);
            statement.setInt(9, 0);
            statement.setString(10, message);
            statement.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final void getApprovedSubmissions(List<PVMEntry> approvedRecords) {
        try (PreparedStatement statement = getConnection().prepareStatement(GET_APPROVED_SUBMISSIONS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    approvedRecords.add(createEntry(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void getUserSubmissions(String username, List<PVMEntry> approvedRecords) {
        try (PreparedStatement statement = getConnection().prepareStatement(GET_SUBMISSIONS_FOR_USERNAME)) {
            statement.setString(1, "%" + username.toLowerCase().trim() + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    approvedRecords.add(createEntry(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void getHiscores(String boss, String username, int groupSize, int enrage, boolean hardMode, List<PVMEntry> entries) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_HISCORES_TIME_ASC)) {
                statement.setString(1, boss);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        if (!resultSet.getString(USERNAMES).contains(username.toLowerCase().trim())) {
                            continue;
                        }
                        if (resultSet.getInt(GROUPSIZE) != groupSize && groupSize != 0) {
                            continue;
                        }
                        if (resultSet.getInt(ENRAGE) != enrage && enrage != 0) {
                            continue;
                        }
                        if (resultSet.getBoolean(HARDMODE) != hardMode) {
                            continue;
                        }
                        entries.add(createEntry(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final PVMEntry createEntry(ResultSet resultSet) throws SQLException {
        return new PVMEntry(
            resultSet.getString(UID),
            resultSet.getString(BOSS),
            resultSet.getInt(TIME),
            resultSet.getString(LINK),
            resultSet.getString(USERNAMES),
            resultSet.getInt(GROUPSIZE),
            resultSet.getInt(ENRAGE),
            resultSet.getBoolean(HARDMODE),
            resultSet.getInt(APPROVED),
            resultSet.getString(REASON),
            resultSet.getTimestamp(TIMESTAMP)
        );
    }
}
