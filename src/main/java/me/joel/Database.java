package me.joel;
import java.sql.*;

public class Database {

    private static Connection conn = null;

    /**
     * Initializes DB
     */
    public static void connect() {
        String url = "jdbc:sqlite:avalon.sqlite";
        try {
            conn = DriverManager.getConnection(url);
            Console.log("Connection to SQLite has been established");

            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS currency(user_id string UNIQUE, wallet int)");
        } catch (SQLException e) {
            Console.warn("Unable to initialize DB");
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Connection to DB
     */
    public static Connection getConnect() {
        String url = "jdbc:sqlite:avalon.sqlite";

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            Console.warn("Unable to connect to DB");
            e.printStackTrace();
        }

        return conn;
    }

    public static int getWallet(String user_id) throws SQLException {
        String sql = ("SELECT wallet FROM currency WHERE user_id=" + user_id);
        ResultSet rs = getConnect().createStatement().executeQuery(sql);

        int bal = rs.getInt(1);

        if (bal <= 0) {
            String reset = "REPLACE INTO currency(user_id, wallet) values (" + user_id + ", 500)";
            getConnect().createStatement().execute(reset);
            return 0;
        }

        getConnect().close();
        return bal;
    }

    /**
     * Perform addition/subtraction on user bank balance
     * @param user_id
     * @param amt Amount that bank balance is to be modified by
     * @throws SQLException
     */
    public static void modifyWallet(String user_id, int amt) throws SQLException {
        // Get balance
        int bal = 0;
        try {
            bal = Database.getWallet(user_id);
        } catch (SQLException ignore) {}

        int new_bal = bal + amt;
        String sql;

        // Reset balance to 500 if < 0
        if (new_bal < 0) sql = "REPLACE INTO currency(user_id, wallet) values (" + user_id + ", 500)";
        else sql = "REPLACE INTO currency(user_id, wallet) values (" + user_id + ", " + new_bal + ")";

        getConnect().createStatement().execute(sql);
        getConnect().close();
    }

}
