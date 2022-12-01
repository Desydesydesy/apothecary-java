

import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
     /**
     * Connect to a sample database
     */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SQL query = new SQL("database.db");
            var result = query
                    .table("employees")
                    .orderBy(new String[] {"first_name"}, "ASC")
                    .select(new String[] {"first_name", "last_name"})
                    .get();
            
            while (result.next()) {
                System.out.println(result.getString("first_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}