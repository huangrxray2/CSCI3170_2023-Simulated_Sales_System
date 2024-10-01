// Database.java

import java.io.*;
import java.util.*;
import java.sql.*;


class Database {

    // Ours is db26.
    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db26?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group26";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToMySQL() throws SQLException, ClassNotFoundException {

        Connection con = null;
        if (con == null || con.isClosed()) {
            try {
                // Load the JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Method form java.sql
                con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
                // If succeed
                // System.out.println("Connect successfully!");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("[Error]: Java MySQL DB Driver not found!!", e);
            } catch (SQLException e) {
                // e is returned by getConnection.
                throw new RuntimeException("Error connecting to the database", e);
            }
            return con;
        }
        throw new RuntimeException("Connection is already established.");
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed successfully!");
            } catch (SQLException e) {
                throw new RuntimeException("Error while closing the connection", e);
            }
        }
    }

    /*

    Load the JDBC Driver for Oracle DBMS
    try { 
        Class.forName("com.mysql.jdbc.Driver"); 
    } catch(Exception x) { 
        System.err.println("Unable to load the driver class!"); 
    }

     */

    /*

    Establish a Connection
    Connection conn = DriverManager.getConnection( 
    "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/username?autoReconnect=true&useSSL=false", 
    "username", 
    "password");

     */

}