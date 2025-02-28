import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = menu(true);
        switch(choice) {
            case 1:
                // rent equipment
                break;
            case 2:
                // return equipment
                break;
            case 3:
                // deliver equipment
                break;
            case 4:
                // pickup equipment
                break;

        }
        scanner.close();
    }

    private static int menu(boolean startup) {
        if (startup) {
            System.out.println("Welcome to the Drone Delivery Manager!");
        }

        System.out.println("What would you like to do?");
        String[] options = {
                "1 - Rent equipment",
                "2 - Return equipment",
                "3 - Deliver equipment",
                "4 - Pickup equipment"
        };

        for (String option : options) {
            System.out.println(option);
        }

        int option;
        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (option >= 1 && option <= options.length) {
                    return option;
                }
            } else {
                scanner.nextLine(); // Consume invalid input
            }
            System.out.println("Invalid selection! Please enter a number between 1 and " + options.length + ".");
        }
    }
}