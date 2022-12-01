
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Desi
 */
public class SQL {
    Connection con = null;
    String selects = "*";
    String wheres = "";
    String orders = "";
    String table = "";
    
    public SQL(String database) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + database;
            // create a connection to the database
            this.con = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (this.con != null) {
                    this.con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public SQL table(String table) {
        this.table = table;
        return this;
    }
    
    public SQL select(String[] columns) {
        this.selects = String.join(", ", columns);
        return this;
    }
    
    public SQL where(String column, String op, String value) {
        if (this.wheres.isEmpty()) {
            this.wheres = column + " " + op + " " + "\"" + value + "\"";
        } else {
            this.wheres += " AND " + column + " " + op + " " + "\"" + value + "\"";
        }
       
        return this;
    }
    
    public SQL orderBy(String[] columns, String direction) {
        this.orders = String.join(", ", columns) + " " + direction;
        return this;
    }
    
    public ResultSet get() throws SQLException {
        Statement statement = this.con.createStatement();
        String sql = "SELECT " + this.selects + " FROM " + this.table;
        
        if (this.wheres.isEmpty() == false) {
            sql = sql + " WHERE " + this.wheres;
        }

        if (this.orders.isEmpty() == false) {
            sql = sql + " ORDER BY " + this.orders;
        }

        ResultSet result = statement.executeQuery(sql);
        return result;
    }
}
