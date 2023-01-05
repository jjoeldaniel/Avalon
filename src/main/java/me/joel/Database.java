package me.joel;
import org.postgresql.util.PSQLException;

import java.sql.*;

public class Database {

    private static Connection conn = null;

    /**
     * Initializes DB
     */
    public static void connect() {
        try {
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS guild_settings(guild_id bigint PRIMARY KEY,  confession_ch bigint, join_ch bigint, leave_ch bigint, mod_ch bigint, insults int, gm_gn int, now_playing int)");
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS starboard_settings(guild_id bigint PRIMARY KEY,  starboard_ch bigint, star_limit int, star_self int)");

        } catch (SQLException e) {
            Console.warn("Failed to initialize DB");
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Connection to DB
     */
    public static Connection getConnect() {
        // Constants

        final String URL = System.getProperty( "DATABASE_URL" );
        final String USER = System.getProperty( "DATABASE_USER" );
        final String PASSWORD = System.getProperty( "DATABASE_PASSWORD" );

        if (conn == null) {
            try {
                conn = DriverManager.getConnection( URL, USER, PASSWORD );
                return conn;
            } catch (SQLException e) {
                Console.warn("Failed to connect to DB");
                return null;
            }
        }

        return conn;
    }

}
