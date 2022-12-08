
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
    private static Connection con = null;
    String selects = "*";
    String wheres = "";
    String orders = "";
    String table = "";
    String joins = "";
    int limit = -1;
    
    public SQL(String database) {
        if (this.con != null) {
            return;
        }
        
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
    
    public SQL leftJoin(String table, String firstCol, String cmp, String secondCol) {
        this.joins += " LEFT JOIN " + table + " ON " + firstCol + " " + cmp + " " + secondCol;
        return this;
    }
    
    private String combineQuery() {
        String sql = "SELECT " + this.selects + " FROM " + this.table;
        
        if (this.joins.isEmpty() == false) {
            sql = sql + this.joins;
        }
        
        if (this.wheres.isEmpty() == false) {
            sql = sql + " WHERE " + this.wheres;
        }

        if (this.orders.isEmpty() == false) {
            sql = sql + " ORDER BY " + this.orders;
        }
                
        if (this.limit != -1) {
            sql = sql + " LIMIT " + limit;
        }
        
        return sql;
    }
    
    public ResultSet get() throws SQLException {
        Statement statement = this.con.createStatement();
        String sql = combineQuery();

        ResultSet result = statement.executeQuery(sql);
        
        return result;
    }
    
    public String toSql() {
        return combineQuery();
    }
    
    public SQL limit(int n) {
        this.limit = n;
        
        return this;
    }
    
    public ResultSet create(String[] columns, String[] values) throws SQLException {
        Statement statement = this.con.createStatement();
        String sql = "INSERT INTO " + table
                + "(" + String.join(",", columns)
                + ") VALUES (" + String.join(",", values) + ")";
        
        statement.executeUpdate(sql);
        
        return this
                .orderBy(new String[] {"id"}, "desc")
                .limit(1)
                .get();
    }
    
    public void insert(String[] columns, String[][] values) throws SQLException {
        Statement statement = this.con.createStatement();
        String sql = "INSERT INTO " + table
                + "(" + String.join(",", columns)
                + ") VALUES ";
        
        for (int i = 0; i < values.length; i++) {
            sql += "(" + String.join(",", values[i]) + ") ";
            
            if (i != values.length - 1) {
                sql += ",";
            }
        }
        
        statement.executeUpdate(sql);
    }
}
