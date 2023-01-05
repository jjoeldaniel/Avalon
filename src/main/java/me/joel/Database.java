package me.joel;
import java.sql.*;

public class Database {

    private static Connection conn = null;

    /**
     * Initializes DB
     */
    public static void connect() {
        try {
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS guild_settings(guild_id string UNIQUE,  confession_ch string, join_ch string, leave_ch string, mod_ch string, insults int, gm_gn int, now_playing int)");
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS starboard_settings(guild_id string UNIQUE,  starboard_ch string, star_limit int, star_self int)");

            String sql =
                    "SELECT name " +
                    "FROM main.sqlite_master " +
                    "WHERE type='table' " +
                    "AND name='guild_settings' OR name='starboard_settings'";
            ResultSet set = getConnect().createStatement().executeQuery(sql);

            int count = 0;
            while (set.next()) {
                count++;
            }

            if (count != 2) Console.info("Successfully initialized DB");

        } catch (SQLException e) {
            Console.warn("Failed to initialize DB");
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Connection to DB
     */
    public static Connection getConnect() {
        String url = "jdbc:sqlite:avalon.sqlite";

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url);
                return conn;
            } catch (SQLException e) {
                Console.warn("Failed to connect to DB");
                return null;
            }
        }

        return conn;
    }

}
