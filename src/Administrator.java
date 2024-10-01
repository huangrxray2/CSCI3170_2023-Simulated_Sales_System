// Administrator.java

import java.io.*;
import java.util.*;
import java.sql.*;
import java.nio.file.Path;
import java.text.*;

class Administrator {

    public static Scanner scanner = new Scanner(System.in);

    public static void showAdminMenu() throws ClassNotFoundException {

        // System.out.println("66666666666666");

        try {

            int choice = 0;
            Connection con = Database.connectToMySQL();
            Statement stmt = con.createStatement();
            //Statement stmt;

            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to the main menu");
            System.out.print("Enter Your Choice: ");

            while (true) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Invalid choice! Please enter a valid integer choice.");
                    scanner.nextLine();
                }
            }

            switch (choice) {

                case 1:
                    // Create all tabels

                    System.out.print("Processing...");

                    try {
                        /*
                        stmt.executeUpdate(
                            "DROP TABLE category;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE manufacturer;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE part;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE salesperson;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE transaction;"
                        );
                        */

                        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");

                        // category(cID: integer, cName: string)
                        stmt.executeUpdate(
                            "CREATE TABLE if not exists category ( " + 
                                "cID INT NOT NULL PRIMARY KEY, " + 
                                "cName VARCHAR(20) NOT NULL " + 
                            ");"
                        );

                        // manufacturer(mID: integer, mName: string, mAddress: String, mPhoneNumber: integer)
                        stmt.executeUpdate(
                            "CREATE TABLE if not exists manufacturer ( " + 
                            "mID INT NOT NULL PRIMARY KEY, " + 
                                "mName VARCHAR(20) NOT NULL, " + 
                                "mAddress VARCHAR(50) NOT NULL, " + 
                                "mPhoneNumber INT NOT NULL " + 
                            ");"
                        );

                        // part(pID: integer, pName: string, pPrice: integer, mID: integer, cID: integer, pWarrantyPeriod: integer, pAvailableQuantity: integer)
                        stmt.executeUpdate(
                            "CREATE TABLE if not exists part ( " + 
                                "pID INT NOT NULL PRIMARY KEY, " + 
                                "pName VARCHAR(20) NOT NULL, " + 
                                "pPrice INT NOT NULL, " + 
                                "mID INT NOT NULL REFERENCES manufacturer(mID), " + 
                                "cID INT NOT NULL REFERENCES category(cID), " + 
                                "pWarrantyPeriod INT NOT NULL, " + 
                                "pAvailableQuantity INT NOT NULL " +                                 
                            ");"
                        );

                        // salesperson(sID: integer, sName: string, sAddress: string, sPhoneNumber: integer, sExperience: integer)
                        stmt.executeUpdate(
                            "CREATE TABLE if not exists salesperson ( " + 
                                "sID INT NOT NULL PRIMARY KEY, " + 
                                "sName VARCHAR(20) NOT NULL, " + 
                                "sAddress VARCHAR(50) NOT NULL, " + 
                                "sPhoneNumber INT NOT NULL, " + 
                                "sExperience INT NOT NULL " + 
                            ");"
                        );

                        // transaction(tID: integer, pID: integer, sID: integer, tDate: date)
                        stmt.executeUpdate(
                            "CREATE TABLE if not exists transaction ( " + 
                                "tID INT NOT NULL PRIMARY KEY, " + 
                                "pID INT NOT NULL REFERENCES part(pID), " + 
                                "sID INT NOT NULL REFERENCES salesperson(sID), " + 
                                "tDate DATE NOT NULL " + 
                            ");"
                        );

                        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1;"); 

                        System.out.println("Done! Database is initialized!\n");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

                case 2:
                    // Delete all tables

                    System.out.print("Processing...");

                    try {

                        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0;"); 

                        stmt.executeUpdate(
                            "DROP TABLE category;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE manufacturer;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE part;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE salesperson;"
                        );
                        stmt.executeUpdate(
                            "DROP TABLE transaction;"
                        );

                        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1;"); 

                        System.out.println("Done! Database is removed!\n");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

                case 3:
                    // Load from datafile

                    System.out.print("\nType in the Source Data Folder Path: ");
                    String path = scanner.nextLine();

                    System.out.print("Processing...");

                    try {
                        //System.out.println("666");
                        processFile(path, "/category.txt", "category", con);
                        //System.out.println("666");
                        processFile(path, "/manufacturer.txt", "manufacturer", con);
                        processFile(path, "/part.txt", "part", con);
                        processFile(path, "/salesperson.txt", "salesperson", con);
                        processFile(path, "/transaction.txt", "transaction", con);
                    } catch (IOException e) {
                        System.out.println("Error while closing the file: " + e.getMessage());
                    } catch (SQLException e) {
                        System.out.println("SQL Exception: " + e.getMessage());
                    }

                    System.out.println("Done! Data is inputted to the database!\n");

                    break;
        
                case 4:
                    // Show content of a table

                    while (true) {
                        System.out.print("Which table would you like to show: ");
                        String tableName = scanner.nextLine().trim();
                        // System.out.print("Processing...");

                        if (tableName.equals("go back")) {
                            break;
                        }

                        try {
                            if (isValidTableName(tableName)) {
                                if (tableExists(tableName, con)) {
                                    System.out.println("Content of table " + tableName + ":");
                                    showTable(tableName, con);
                                    break;
                                } else {
                                    System.out.println("Table " + tableName + " does not exist.");
                                }
                            } else {
                                System.out.println("Invalid table name. Please try again.");
                                System.out.println("Input \"go back\" to return");
                            }
                        } catch (SQLException e) {
                            System.out.println("SQL Exception: " + e.getMessage());
                        }
                    }
        
                    break;
                case 5:
                    // Return to the main menu
                    return;
                default:
                    System.out.printf("Not a valid admin choice: You entered %d\n", choice);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(String path, String fileName, String tableName, Connection con) throws IOException, SQLException {

        String insertQuery = "";
        int numberOfColumns = 0;

        if ("category".equals(tableName)) {
            insertQuery = "INSERT INTO category VALUES (?, ?)";
            numberOfColumns = 2;
        } else if ("manufacturer".equals(tableName)) {
            insertQuery = "INSERT INTO manufacturer VALUES (?, ?, ?, ?)";
            numberOfColumns = 4;
        } else if ("part".equals(tableName)) {
            insertQuery = "INSERT INTO part VALUES (?, ?, ?, ?, ?, ?, ?)";
            numberOfColumns = 7;
        } else if ("salesperson".equals(tableName)) {
            insertQuery = "INSERT INTO salesperson VALUES (?, ?, ?, ?, ?)";
            numberOfColumns = 5;
        } else if ("transaction".equals(tableName)) {
            insertQuery = "INSERT INTO transaction VALUES (?, ?, ?, ?)";
            numberOfColumns = 4;
        }

        String fullPath = "./" + path + fileName;
        // System.out.println(fullPath);

        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0;"); 

        try (Scanner scanFile = new Scanner(new File(fullPath));
             PreparedStatement pstmt = con.prepareStatement(insertQuery)) {

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\t");
                if (data.length == numberOfColumns) {
                    for (int i = 0; i < numberOfColumns; i++) {
                        if (i == 3 && "transaction".equals(tableName)) {
                            // Format the date string to 'yyyy-MM-dd' format
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                java.util.Date date = sdf.parse(data[i]);
                                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                                pstmt.setDate(i + 1, sqlDate);
                            } catch (ParseException e) {
                                System.out.println("Error parsing date: " + e.getMessage());
                            }
                        } else {
                            pstmt.setString(i + 1, data[i]);
                        }
                    }
                    pstmt.executeUpdate();
                } else {
                    System.out.println("Invalid data in file: " + fileName);
                }
            }

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1;"); 

        } catch (IOException e) {
            System.out.println("Error while processing file: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            System.out.println("SQL Exception while processing file: " + e.getMessage());
            throw e;
        }
    }

    private static boolean isValidTableName(String tableName) {
        
        if (tableName == null) {
            return false;
        }

        if (tableName.equals("category")) {
            return true;
        }
        else if (tableName.equals("manufacturer")) {
            return true;
        }
        else if (tableName.equals("part")) {
            return true;
        }
        else if (tableName.equals("salesperson")) {
            return true;
        }
        else if (tableName.equals("transaction")) {
            return true;
        }
        return false;
    }

    private static boolean tableExists(String tableName, Connection con) throws SQLException {

        DatabaseMetaData metaData = con.getMetaData();
        ResultSet tables = metaData.getTables(null, null, tableName, null);
        return tables.next();
    }

    private static void showTable(String tableName, Connection con) throws SQLException {

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.print("| " + metaData.getColumnName(i) + " ");
            }
            System.out.println("|");

            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    Object value = rs.getObject(i);
                    String stringValue;
                    if (value != null) {
                        stringValue = value.toString();
                    } else {
                        stringValue = "NULL";
                    }
                    System.out.print("| " + stringValue + " ");
                }
                System.out.println("|");
            }
        }
        System.out.println("End of Query\n");
    }
}