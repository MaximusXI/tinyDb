import Screen.PrintScreen;

import java.util.Scanner;


/**
 * The entry point of the application.
 * <p>
 * This class contains the {@code main} method which serves as the starting point for the application.
 * It initializes the {@link PrintScreen} and invokes the {@code MainScreen} method to display
 * the main interface to the user.
 * </p>
 */
public class Main {
    public static void main(String[] args) {

        PrintScreen screen = new PrintScreen();
        screen.MainScreen(new Scanner(System.in));
    }
}