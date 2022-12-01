package app;

import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
     /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
            String sql = "SELECT FirstName, LastName FROM employees";
            try (Connection openConn = conn;
                Statement stmt  = openConn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql)){
 
                // loop through the result set
                while (rs.next()) {
                    System.out.println(rs.getString("FirstName") + "\t" +
                                       rs.getString("LastName"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }
}