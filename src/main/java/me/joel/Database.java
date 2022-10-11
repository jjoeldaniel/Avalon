package me.joel;
import java.sql.*;

public class Database {

    private static Connection conn = null;

    /**
     * Initializes DB
     */
    public static void connect() {
        try {
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS currency(user_id string UNIQUE, wallet int)");
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS guild_settings(guild_id string UNIQUE,  confession_ch string, join_ch string, leave_ch string)");
            getConnect().createStatement().execute("CREATE TABLE IF NOT EXISTS starboard_settings(guild_id string UNIQUE,  starboard_ch string, star_limit int, star_self int)");

            Console.info("Successfully initialized DB");
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

    public static int getWallet(String user_id) throws SQLException {
        try {
            String sql = ("SELECT wallet FROM currency WHERE user_id=" + user_id);
            ResultSet rs = getConnect().createStatement().executeQuery(sql);

            int bal = rs.getInt(1);

            if (bal < 100) {
                String reset = "REPLACE INTO currency(user_id, wallet) values (" + user_id + ", 500)";
                getConnect().createStatement().execute(reset);
                return 0;
            }

            return bal;
        } catch (SQLException e) {
            Console.warn("Failed to connect to DB");
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Perform addition/subtraction on user bank balance
     * @param user_id Discord user ID
     * @param amt Amount that bank balance is to be modified by
     * @throws SQLException Error attempting to communicate with database
     */
    public static void modifyWallet(String user_id, int amt) throws SQLException {
        // Get balance
        int bal = 0;
        try {
            bal = Database.getWallet(user_id);
        } catch (SQLException ignore) {}

        // Reset balance to 500 if < 100
        int new_bal = bal + amt;
        if (new_bal < 100) new_bal = 500;

        String sql = "REPLACE INTO currency(user_id, wallet) values (" + user_id + ", " + new_bal + ")";
        getConnect().createStatement().execute(sql);
    }

}
