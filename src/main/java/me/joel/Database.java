package me.joel;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {

    private static Connection conn = null;

    // SL4FJ Logger
    final private static Logger log = LoggerFactory.getLogger( Database.class );

    /**
     * Initializes DB
     */
    public static void initialize() throws SQLException {

        try ( Connection conn = getConnect() ) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS guild_settings(guild_id bigint PRIMARY KEY,  confession_ch bigint, join_ch bigint, leave_ch bigint, mod_ch bigint, insults int, gm_gn int, now_playing int)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS starboard_settings(guild_id bigint PRIMARY KEY,  starboard_ch bigint, star_limit int, star_self int)");
        }

    }

    /**
     * @return Connection to DB
     */
    public static Connection getConnect() {
        // Constants

        final String URL = System.getenv( "DATABASE_URL" );
        final String USER = System.getenv( "DATABASE_USER" );
        final String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

        if (conn == null) {

            try {
                conn = DriverManager.getConnection( URL, USER, PASSWORD );
                return conn;
            } catch (SQLException e) {
                log.error( "Error connecting to database", e );
                return null;
            }
        }

        return conn;
    }

}
