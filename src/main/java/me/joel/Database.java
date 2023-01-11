package me.joel;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {


    // SL4FJ Logger
    final private static Logger log = LoggerFactory.getLogger( Database.class );

    final private static String URL = System.getenv( "DATABASE_URL" );
    final private static String USER = System.getenv( "DATABASE_USER" );
    final private static String PASSWORD = System.getenv( "DATABASE_PASSWORD" );

    /**
     * Initializes DB
     */
    public static void initialize() throws SQLException {

        try ( Connection conn = DriverManager.getConnection( URL, USER, PASSWORD ) ) {
            conn.createStatement().execute( "CREATE TABLE IF NOT EXISTS \"public\".\"guild_settings\"(\n" +
                        "    guild_id BIGINT PRIMARY KEY,\n" +
                        "    insults BOOLEAN NOT NULL,\n" +
                        "    gm_gn BOOLEAN NOT NULL,\n" +
                        "    now_playing BOOLEAN NOT NULL\n" +
                        ");");
        }
        catch ( SQLException e ) {
            log.error( "Error connecting/initializing database", e );
            throw e;
        }
    }
}
