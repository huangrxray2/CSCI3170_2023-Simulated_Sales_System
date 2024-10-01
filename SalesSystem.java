// SalesSystem.java

import java.io.*;
import java.util.*;
import java.sql.*;

class SalesSystem {

        public static Scanner scanner = new Scanner(System.in);

        private static Connection con;

        private static boolean showMainMenu() {

                int choice = 0;
                String keyword = "";

                System.out.println("Welcome to sales system!\n");
                System.out.println("-----Main menu-----");
                System.out.println("What kinds of operation would you like to perform?");
                System.out.println("1. Operations for administrator");
                System.out.println("2. Operations for salesperson");
                System.out.println("3. Operations for manager");
                System.out.println("4. Exit this program");

                // Proj Specification: Your Java program may assume that any value entered into any input field is correct in format only.

                System.out.print("Enter Your Choice: ");
                if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                }
                else {
                        if (scanner.hasNextLine()) {
                                scanner.nextLine(); // Clear invalid input
                        }
                        System.out.println("Input error: Not an int!");
                }

                System.out.println("");

                switch (choice) {
                        case 1:
                                //Administrator.
                                try {
                                        Administrator.showAdminMenu();
                                } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                }
                                break;
                        case 2:
                                //Salesperson.
                                try {
                                        Salesperson.showSalespersonMenu();
                                } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                }
                                break;
                        case 3:
                                //Manager.
                                try {
                                        Manager.showManagerMenu();
                                } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                }
                                break;
                        case 4:
                                return true;
                        default:
                                System.out.println("Main menu choice error: ");
                }
                return false;
        }


        public static void main(String[] args) throws ClassNotFoundException {

                boolean exit = false;

                while (!exit) {
                        exit = showMainMenu();
                }

                if (con != null) {
                        try {
                                con.close();
                        } catch (SQLException e) {
                                System.out.println("Failed to close the database connection: " + e.getMessage());
                        }
                }                
        }
}