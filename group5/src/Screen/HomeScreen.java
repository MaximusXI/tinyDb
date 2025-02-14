package Screen;

import service.DatabaseService;

import java.util.Scanner;

/**
 * Represents the home screen interface of the database system application.
 * <p>
 * The {@code HomeScreen} class provides a console-based user interface to interact with the database system.
 * It allows users to execute queries, export data and structure, generate an Entity-Relationship Diagram (ERD),
 * and exit the application. The class relies on {@link DatabaseService} to perform operations related to
 * database queries, data export, and ERD generation.
 * </p>
 */
public class HomeScreen {
    Scanner sc;
    DatabaseService databaseService;

    /**
     * Constructs a new {@code HomeScreen} instance with the specified {@link Scanner} for user input.
     *
     * @param sc the {@link Scanner} instance to read user input
     */
    public HomeScreen(Scanner sc) {
        this.sc = sc;
        databaseService = new DatabaseService();
    }

    /**
     * Loads and displays the home screen menu, allowing users to choose different actions.
     * <p>
     * The method presents a menu with options to:
     * <ul>
     *     <li>Write SQL queries.</li>
     *     <li>Export database data and structure.</li>
     *     <li>Generate an Entity-Relationship Diagram (ERD).</li>
     *     <li>Exit the application.</li>
     * </ul>
     * Users can enter their choice, and the corresponding action will be executed. The method will continue
     * to display the menu until the user chooses to exit.
     * </p>
     *
     * @param userId the ID of the user performing the actions
     * @throws Exception if an error occurs while processing user input or executing actions
     */
    public void loadHomeScreen(String userId) throws Exception {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Database System");
            System.out.println("1. Write Queries");
            System.out.println("2. Export Data and Structure");
            System.out.println("3. ERD");
            System.out.println("4. Exit");
            int input = 10;
            try {
                input = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter valid integer");
                sc.nextLine();
                continue;
            }
            switch (input) {
                case 1:
                    sc.nextLine();
                    System.out.println("Please enter the query");
                    StringBuilder query = new StringBuilder();
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        query.append(line).append("\n");
                        if (line.trim().endsWith(";")) {
                            break;
                        }
                    }
                    databaseService.processQueries(query.toString(), userId);
                    System.out.println("The query entered is:" + query);
                    continue;
                case 2:
                    System.out.println("Export the Data and Structure");
                    boolean succesfulDumpGeneration = databaseService.DumpDB();
                    if(succesfulDumpGeneration){
                        System.out.println("Successfully created sql dump for tables and its records");
                    }
                    else {
                        System.out.println("Error occurred while generating sql dumb for tables and its records");
                    }
                    continue;
                case 3:
                    sc.nextLine();
                    System.out.println("Please enter the database name for which you want to generate the ERD:");
                    String databaseName = sc.nextLine();
                    System.out.println("Generating ERD for database: " + databaseName);
                    databaseService.generateERD("foreignkey.txt", "database.txt", databaseName);
                    continue;
                case 4:
                    System.out.println("Successfully Exit");
                    running = false;
                    break;
                default:
                    System.out.println("Retry");
            }
        }
    }
}
