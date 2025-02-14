package Screen;

import Utils.*;
import Model.User;
import Constants.*;

import java.io.IOException;
import java.util.*;

/**
 * This class represents the main screen of the application, handling user authentication, registration,
 * and navigating to the home screen.
 */
public class PrintScreen {
    SecurityQuestion questions;
    EncryptionAlgorithm encryptionAlgorithm = new EncryptionAlgorithm();
    IOHandler ioHandler = new IOHandler();
    TempBuffer buffer;
    HomeScreen homeScreen;

    public PrintScreen() {
        questions = new SecurityQuestion();
        encryptionAlgorithm = new EncryptionAlgorithm();
        ioHandler = new IOHandler();
        buffer = new TempBuffer();
    }

    /**
     * Shows main screen menu to navigate further into sub menu for example: registration, login.
     *
     * @param scanner The scanner object for reading user input.
     */
    public void MainScreen(Scanner scanner) {
        int option;
        boolean validEntry = false;
        try {

            if (scanner != null) {
                do {
                    System.out.println("\n\n******* TINY DATABASE - MAIN MENU *******\n");
                    System.out.println("1. User Login");
                    System.out.println("2. Registration");
                    System.out.println("0. Exit\n");
                    System.out.print("Select an option: ");

                    if (scanner.hasNextInt()) {
                        option = scanner.nextInt();
                        if (scanner.hasNextLine()) {
                            scanner.nextLine();
                        }
                        validEntry = true;
                        switch (option) {
                            case 1:
                                AuthenticateUserScreen(scanner);
                                break;
                            case 2:
                                RegistrationScreen();
                                break;
                            case 0:
                                System.out.println("Exiting tinydb....");
                                break;
                            default:
                                System.err.println("Entered option does not exist. Please try again.");
                        }
                    } else {
                        validEntry = false;
                        option = -1;
                        scanner.nextLine();
                        System.err.println("You have entered invalid option value. Please try again.");
                    }
                } while (option != 0 || !validEntry);

                scanner.close();
            }
        } catch (Exception e) {
            new Exception(e.getMessage());
        }
    }
    /**
     * Shows authenticate user screen menu, where user will enter credentials to login
     *
     * @param scanner The scanner object for reading user input.
     */
    public void AuthenticateUserScreen(Scanner scanner) {
        Map<String, User> users = buffer.LoadUserProfileData();
        boolean authenticated;
        //Scanner scanner = new Scanner(System.in);
        User user;
        String userId="";
        while (true) {
            System.out.println("\n\n******* LOGIN - PAGE *******\n");
            System.out.println("Enter your username: ");
            userId = scanner.nextLine();
            String HashedUserId = encryptionAlgorithm.hashData(userId);

            if (!users.containsKey(HashedUserId)) {
                System.out.println(Constants.INCORRECTUSERID);
                if (!tryAgain(scanner)) {
                    System.out.println(Constants.RETURNTOMAINMENU);
                    return;
                }
                continue;
            }

            user = users.get(HashedUserId);
            System.out.println("Enter your password: ");
            String password = encryptionAlgorithm.hashData(scanner.nextLine());

            if (!user.getPassword().equals(password)) {
                System.out.println(Constants.INCORRECTPASSWORD);
                if (!tryAgain(scanner)) {
                    System.out.println(Constants.RETURNTOMAINMENU);
                    return;
                }
                continue;
            }

            Random random = new Random();
            int randomNumber = random.nextInt(3) + 1;
            System.out.println(questions.getQuestion(randomNumber));
            String answer = encryptionAlgorithm.hashData(scanner.nextLine());

            if (!user.getSecurityAnswers().get(randomNumber).equals(answer)) {
                System.out.println(Constants.INCORRECTANSWER);
                if (!tryAgain(scanner)) {
                    System.out.println(Constants.RETURNTOMAINMENU);
                    return;
                }
                continue;
            }

            authenticated = true;
            break;
        }
        if (authenticated) {
            System.out.println("Login successful!");
            QueryLogger.logGeneral("USER AUTHENTICATION", "User Authenticated Successfully for " + userId);
            homeScreen = new HomeScreen(scanner);
            try {
                homeScreen.loadHomeScreen(userId);
            } catch (Exception e) {
                System.out.println("Invalid Input please Try again");
            }
        }
    }
    /**
     * Shows try again option, on entering incorrect data
     *
     * @param scanner The scanner object for reading user input.
     */
    private boolean tryAgain(Scanner scanner) {
        while (true) {
            String response = scanner.nextLine();
            if (response.equals(Constants.YES)) {
                return true;
            } else if (response.equals(Constants.NO)) {
                return false;
            } else {
                System.out.println(Constants.INVALIDINPUT);
            }
        }
    }
    /**
     * Shows registration menu, where user will enter details to registered themselves
     *
     * @throw IOException for reading the data from file.
     *
     */
    public void RegistrationScreen() throws IOException {
        Map<String, User> users = buffer.LoadUserProfileData();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String userId = scanner.nextLine();
        String HashedUserId = encryptionAlgorithm.hashData(userId);


        while (true) {
            if (users.containsKey(HashedUserId)) {
                System.out.println("UserId already exists! Do you want to use another userId? (yes/no)");
                if (!tryAgain(scanner)) {
                    System.out.println("Exiting to main menu...");
                    return;
                }
                continue;
            }
            break;
        }

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        Map<Integer, String> securityAnswers = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            System.out.println(questions.getQuestion(i));
            String answer = scanner.nextLine();
            securityAnswers.put(i, encryptionAlgorithm.hashData(answer));
        }

        String hashedUserId = encryptionAlgorithm.hashData(userId);
        String hashedPassword = encryptionAlgorithm.hashData(password);

        User user = new User(hashedUserId, hashedPassword, securityAnswers);
        boolean userRegistered = ioHandler.WriteUserDataToFile(user);

        if (userRegistered) {
            QueryLogger.logGeneral("USER REGISTRATION", "User Registered Successfully");
            System.out.println("User Registered Successfully.");
        } else {
            System.err.println("There was a problem in registering the user.");
        }

    }
}
