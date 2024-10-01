// Manager.java

import java.io.*;
import java.util.*;
import java.sql.*;


class Manager {

    public static Scanner scanner = new Scanner(System.in);

    public static void showManagerMenu() throws ClassNotFoundException {

        try {

            int choice = 0;
            Connection con = Database.connectToMySQL();
            Statement stmt = con.createStatement();

            System.out.println("-----Operations for manager menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println("2. Count the no. of sales record of each salesperson under a specific range on years of experience");
            System.out.println("3. Show the total sales value of each manufacturer");
            System.out.println("4. Show the N most popular part");
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
                    // List all salespersons

                    String salesperSonOrder = "";

                    while (true) {
                        System.out.println("Choose Ordering:");
                        System.out.println("1. By ascending order");
                        System.out.println("2. By descending order:");
                        System.out.print("Choose the list ordering: ");
                        salesperSonOrder = scanner.nextLine().trim();

                        if (salesperSonOrder.equals("go back")) {
                            return;
                        }
                        if (salesperSonOrder.equals("1") || salesperSonOrder.equals("2")) {
                            break;
                        } 
                        else {
                            System.out.println("Invalid salesperson order. Please try again.");
                            System.out.println("Input \"go back\" to return");
                        }
                    }

                    if (salesperSonOrder.equals("1")) {
                        salesperSonOrder = "ASC";
                    }
                    else if (salesperSonOrder.equals("2")) {
                        salesperSonOrder = "DESC";
                    }
                    else {
                        System.out.println("Not a valid salesperson order!");
                    }

                    System.out.println("| ID | Name | Mobile Phone | Years of Experience |");

                    try (ResultSet rs = stmt.executeQuery("SELECT sID, sName, sPhoneNumber, sExperience FROM salesperson ORDER BY sExperience " + salesperSonOrder + ";")) {

                            ResultSetMetaData metaData = rs.getMetaData();
                            int numberOfColumns = metaData.getColumnCount();

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

                    break;

                case 2:
                    // Count the no. of sales record of each salesperson under a specific range on years of experience

                    int lowerBound = 0;
                    int upperBound = 0;
                    String returnCommand = "";

                    while (true) {
                        System.out.print("Type in the lower bound for years of experience: ");
                        lowerBound = scanner.nextInt();
                        scanner.nextLine();

                        if (lowerBound >= 0) {
                            break;
                        } 
                        else {
                            System.out.println("Invalid lower bound. Please try again.");
                            System.out.println("Input \"go back\" to return");
                            returnCommand = scanner.nextLine().trim();
                            if (returnCommand.equals("go back")) {
                                return;
                            }
                        }
                    }
                    
                    while (true) {
                        System.out.print("Type in the upper bound for years of experience: ");
                        upperBound = scanner.nextInt();
                        scanner.nextLine();

                        if (upperBound >= lowerBound) {
                            break;
                        } 
                        else {
                            System.out.println("Invalid upper bound. Please try again.");
                            System.out.println("Input \"go back\" to return");
                            returnCommand = scanner.nextLine().trim();
                            if (returnCommand.equals("go back")) {
                                return;
                            }
                        }

                    }

                    System.out.println("Transaction Record:");

                    System.out.println("| ID | Name | Years of Experience | Numbers of Transaction |");

                    try (ResultSet rs = stmt.executeQuery(
                            "SELECT salesperson.sID, sName, sExperience, COUNT(tID) AS numOfTransactions " + 
                            "FROM salesperson LEFT JOIN transaction " + 
                            "ON salesperson.sID = transaction.sID " + 
                            "WHERE sExperience >= " + 
                            lowerBound + 
                            " AND sExperience <= " + 
                            upperBound + 
                            " GROUP BY sID, sName, sExperience " + 
                            "ORDER BY sID DESC;")) {

                            ResultSetMetaData metaData = rs.getMetaData();
                            int numberOfColumns = metaData.getColumnCount();

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

                    break;

                case 3:
                    // Show the total sales value of each manufacturer

                    System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");

                    try (ResultSet rs = stmt.executeQuery(
                            "SELECT m.mID, mName, COALESCE(SUM(p.pPrice), 0) AS totalSalesValue " + 
                            "FROM manufacturer m " + 
                            "RIGHT JOIN part p ON m.mID = p.mID " + 
                            "RIGHT JOIN transaction t ON p.pID = t.pID " + 
                            "GROUP BY mID, mName " + 
                            "ORDER BY totalSalesValue DESC;")) {

                            ResultSetMetaData metaData = rs.getMetaData();
                            int numberOfColumns = metaData.getColumnCount();

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

                    break;

                case 4:
                    // Show the N most popular part

                    int popularPartNum = 0;

                    System.out.print("Type in the number of parts: ");
                    popularPartNum = scanner.nextInt();

                    System.out.println("| Part ID | Part Name | No. of Transaction |");

                    try (ResultSet rs = stmt.executeQuery(
                            "SELECT p.pID, p.pName, COUNT(t.tID) AS totalTransactions " + 
                            "FROM part p " + 
                            "LEFT JOIN transaction t ON p.pID = t.pID " + 
                            "GROUP BY p.pID, p.pName " + 
                            "ORDER BY totalTransactions DESC " + 
                            "LIMIT " + popularPartNum + ";")) {

                            ResultSetMetaData metaData = rs.getMetaData();
                            int numberOfColumns = metaData.getColumnCount();

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

                    break;
                case 5:
                    // Return to the main menu
                    return;
                default:
                    System.out.printf("Not a valid manager choice: You entered %d", choice);
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}