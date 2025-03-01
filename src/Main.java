import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Welcome to the Drone Delivery Manager!");

        while (running) {
            int choice = menu();
            switch(choice) {
                case 1:
                    rentEquipment();
                    break;
                case 2:
                    returnEquipment();
                    break;
                case 3:
                    deliverEquipment();
                    break;
                case 4:
                    pickupEquipment();
                    break;
                case 5:
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection!");
            }
        }

        scanner.close();
    }

    private static int menu() {
        System.out.println();
        System.out.println("What would you like to do?");
        String[] options = {
                "1 - Rent equipment (NOOP)",
                "2 - Return equipment (NOOP)",
                "3 - Deliver equipment (NOOP)",
                "4 - Pick up equipment (NOOP)",
                "5 - Equipment manager",
                "6 - Drone manager",
                "7 - Exit"
        };

        for (String option : options) {
            System.out.println(option);
        }

        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int selected = scanner.nextInt();
                scanner.nextLine();
                if (selected >= 1 && selected <= 5) {
                    return selected;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid selection! Please enter a number between 1 and 5.");
        }
    }

    private static void rentEquipment() {
        System.out.println("Renting equipment... [No operation]");
    }

    private static void returnEquipment() {
        System.out.println("Returning equipment... [No operation]");
    }

    private static void deliverEquipment() {
        System.out.println("Delivering equipment... [No operation]");
    }

    private static void pickupEquipment() {
        System.out.println("Picking up equipment... [No operation]");
    }
}
