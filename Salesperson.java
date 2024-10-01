// Salesperson.java

import java.io.*;
import java.util.*;
import java.sql.*;


class Salesperson {

    public static Scanner scanner = new Scanner(System.in);

    public static void showSalespersonMenu() throws ClassNotFoundException {

        try {

            int choice = 0;
            Connection con = Database.connectToMySQL();
            Statement stmt = con.createStatement();
            ResultSet rs = null;

            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for parts");
            System.out.println("2. Sell a part");
            System.out.println("3. Return to the main menu");
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
                    // Search for parts

                    String criterion = "";
                    String keyword = "";
                    String order = "";

                    while (true) {
                        System.out.println("Choose the Search Criterion:");
                        System.out.println("1. Part Name");
                        System.out.println("2. Manufacturer Name");
                        System.out.print("Choose the Search Criterion: ");
                        criterion = scanner.nextLine().trim();

                        if (criterion.equals("go back")) {
                            return;
                        }
                        if (criterion.equals("1") || criterion.equals("2")) {
                            break;
                        } 
                        else {
                            System.out.println("Invalid criterion. Please try again.");
                            System.out.println("Input \"go back\" to return");
                        }
                    }

                    while (criterion.equals("1")) {
                        System.out.print("Type in the Search Keyword: ");
                        keyword = scanner.nextLine().trim();

                        if (keyword.equals("go back")) {
                            return;
                        }
                        String checkMatch = "SELECT * FROM part WHERE pName LIKE '%" + keyword + "%';";
                        // System.out.println(checkMatch);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(checkMatch);
                        if (rs.next()) {
                            break;
                        }
                        else {
                            System.out.println("Invalid part. Please try again.");
                            System.out.println("Input \"go back\" to return");
                        }
                    }

                    while (criterion.equals("2")) {
                        System.out.print("Type in the Search Keyword: ");
                        keyword = scanner.nextLine().trim();

                        if (keyword.equals("go back")) {
                            return;
                        }
                        String checkMatch = "SELECT * FROM manufacturer WHERE mName LIKE '%" + keyword + "%';";
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(checkMatch);
                        if (rs.next()) {
                            break;
                        }
                        else {
                            System.out.println("Invalid manufacturer. Please try again.");
                            System.out.println("Input \"go back\" to return");
                        }
                    }

                    while (true) {
                        System.out.println("Choose Ordering:");
                        System.out.println("1. By price, ascending order");
                        System.out.println("2. By price, descending order");
                        System.out.print("Choose the Search Criterion: ");
                        order = scanner.nextLine().trim();

                        if (order.equals("go back")) {
                            return;
                        }
                        if (order.equals("1") || order.equals("2")) {
                            break;
                        } 
                        else {
                            System.out.println("Invalid order. Please try again.");
                            System.out.println("Input \"go back\" to return");
                        }
                    }

                    searchForParts(criterion, keyword, order);
                    System.out.println("End of Query\n");

                    break;

                case 2:
                    // Sell a part

                    String pid = "";
                    String sid = "";
                    String returnCommand = "";

                    while (true) {
                        System.out.print("Enter the Part ID: ");
                        pid = scanner.nextLine().trim();

                        if (pid.equals("go back")) {
                            return;
                        }
                        String checkMatch = "SELECT * FROM part WHERE pID = " + pid + ";";
                        try {
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(checkMatch);
                            if (rs.next()) {
                                break;
                            } else {
                                System.out.println("Invalid pid. Please try again.");
                                System.out.println("Input \"go back\" to return");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    while (true) {
                        System.out.print("Enter the Salesman ID: ");
                        sid = scanner.nextLine().trim();

                        if (sid.equals("go back")) {
                            return;
                        }
                        String checkMatch = "SELECT * FROM salesperson WHERE sID = " + sid + ";";
                        // System.out.println(checkMatch);
                        try {
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(checkMatch);
                            if (rs.next()) {
                                break;
                            } else {
                                System.out.println("Invalid sid. Please try again.");
                                System.out.println("Input \"go back\" to return");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    while (true) {
                        try {
                            String checkQuery = "SELECT pAvailableQuantity, pId, pName FROM part WHERE pID = " + pid + ";";
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(checkQuery);
                    
                            if (rs.next()) {
                                int availableQuantity = rs.getInt("pAvailableQuantity");
                                int soldPartID = rs.getInt("pID");
                                String soldPartName = rs.getString("pName");
                                if (availableQuantity == 0) {
                                    System.out.println("No parts in stock!");
                                    System.out.println("Invalid quantity. Please return.");
                                    System.out.println("Input \"go back\" to return");
                                    returnCommand = scanner.nextLine().trim();
                                    if (returnCommand.equals("go back")) {
                                        break;
                                    }
                                } else {

                                    String saleQuery = "UPDATE part SET pAvailableQuantity = pAvailableQuantity - 1 WHERE pID = " + pid + ";";
                                    stmt.executeUpdate(saleQuery);

                                    System.out.print("Product: ");
                                    System.out.print(soldPartName);
                                    System.out.print("(id: ");
                                    System.out.print(soldPartID);
                                    System.out.print(") ");
                                    System.out.print("Remaining Quantity: ");
                                    System.out.print(availableQuantity - 1 + "\n");
                                    System.out.println("End of Query\n");
                    
                                    break;
                                }
                            } else {
                                System.out.println("Invalid pid. Please try again.");
                                System.out.println("Input \"go back\" to return");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case 3:
                    // Return to the main menu
                    return;
                default:
                    System.out.printf("Not a valid salesperson choice: You entered %d", choice);
                    break;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchForParts(String criterion, String keyword, String order) throws SQLException {

        String searchPartQuery = "";

        if (criterion.equals("1")) {
            if (order.equals("1")) {
                searchPartQuery = 
                "SELECT pID, pName, mName, cName, pAvailableQuantity, pWarrantyPeriod, pPrice " + 
                "FROM part " + 
                "JOIN manufacturer ON part.mID = manufacturer.mID " + 
                "JOIN category ON part.cID = category.cID " + 
                "WHERE pName " + 
                "LIKE '%" + 
                keyword + 
                "%' " + 
                "ORDER BY pPrice ASC;";
            } else if (order.equals("2")) {
                searchPartQuery = 
                "SELECT pID, pName, mName, cName, pAvailableQuantity, pWarrantyPeriod, pPrice " + 
                "FROM part " + 
                "JOIN manufacturer ON part.mID = manufacturer.mID " + 
                "JOIN category ON part.cID = category.cID " + 
                "WHERE pName " + 
                "LIKE '%" + 
                keyword + 
                "%' " + 
                "ORDER BY pPrice DESC;";
            } else {
                System.out.println("Not correct order argument!");
            }
        } else if (criterion.equals("2")) {
            if (order.equals("1")) {
                searchPartQuery = 
                "SELECT pID, pName, mName, cName, pAvailableQuantity, pWarrantyPeriod, pPrice " + 
                "FROM part " + 
                "JOIN manufacturer ON part.mID = manufacturer.mID " + 
                "JOIN category ON part.cID = category.cID " + 
                "WHERE mName " +
                "LIKE '%" + 
                keyword + 
                "%' " + 
                "ORDER BY pPrice ASC;";
            } else if (order.equals("2")) {
                searchPartQuery = 
                "SELECT pID, pName, mName, cName, pAvailableQuantity, pWarrantyPeriod, pPrice " + 
                "FROM part " + 
                "JOIN manufacturer ON part.mID = manufacturer.mID " + 
                "JOIN category ON part.cID = category.cID " + 
                "WHERE mName " +
                "LIKE '%" + 
                keyword + 
                "%' " + 
                "ORDER BY pPrice DESC;";
            } else {
                System.out.println("Not correct order argument!");
            }
        } else {
            System.out.println("Not correct criterion argument!");
        }

        System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price |");

        try (Connection con = Database.connectToMySQL();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(searchPartQuery)) {

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
        } catch (ClassNotFoundException e) {
            System.out.println("error!");
        }
    }
}