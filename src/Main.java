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
                    droneMenu();
                    break;
                case 6:
                    equipmentMenu();
                    break;
                case 7:
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
                "5 - Drone manager",
                "6 - Equipment manager",
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
                if (selected >= 1 && selected <= options.length) {
                    return selected;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid selection! Please enter a number between 1 and " + options.length + ".");
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
    /*
     *  DRONE MENU
     */
    private static void droneMenu() {
        boolean backToMain = false;
        while (!backToMain) {
            switch (showDroneMenu()) {
                case 1:
                    addDrone();
                    break;
                case 2:
                    editDrone();
                    break;
                case 3:
                    deleteDrone();
                    break;
                case 4:
                    searchDrone();
                    break;
                case 5:
                    DroneManager.listAll();
                    break;
                case 6:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static int showDroneMenu() {
        System.out.println("\n=== Drone Menu ===");
        System.out.println("1 - Add drone");
        System.out.println("2 - Edit drone");
        System.out.println("3 - Delete drone");
        System.out.println("4 - Search drone");
        System.out.println("5 - List all drones");
        System.out.println("6 - Back to main menu");
        System.out.print("Enter choice: ");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } else {
            scanner.nextLine(); // consume invalid input
            return -1;
        }
    }

    // Add drone
    private static void addDrone() {
        System.out.print("Enter Drone ID: ");
        String droneId = scanner.nextLine();

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter Max Payload (numeric): ");
        double maxPayload = 0.0;
        try {
            maxPayload = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for payload, defaulting to 0.0.");
        }

        System.out.print("Enter Battery Life (numeric): ");
        double batteryLife = 0.0;
        try {
            batteryLife = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for battery life, defaulting to 0.0");
        }

        System.out.print("Enter Status (e.g. Available, In Use): ");
        String status = scanner.nextLine();

        System.out.print("Enter Location: ");
        String location = scanner.nextLine();

        // Create new Drone object
        Drone newDrone = new Drone(droneId, model, maxPayload, batteryLife, status, location);

        // Add to DroneManager
        DroneManager.addDrone(newDrone);
    }

    // Edit drone
    private static void editDrone() {
        System.out.print("Enter the Drone ID of the drone to edit: ");
        String serialNumber = scanner.nextLine();

        Drone existing = DroneManager.findBySerialNumber(serialNumber);
        if (existing == null) {
            System.out.println("No drone found with ID: " + serialNumber);
            return;
        }

        System.out.println("Leave blank to keep current value.");

        // Model
        System.out.print("Enter new Model [" + existing.getModel() + "]: ");
        String newModel = scanner.nextLine();
        if (!newModel.isEmpty()) {
            existing.setModel(newModel);
        }

        // Max Payload (numeric)
        System.out.print("Enter new Max Payload [" + existing.getMaxPayload() + "]: ");
        String inputPayload = scanner.nextLine();
        if (!inputPayload.isEmpty()) {
            try {
                double newPayload = Double.parseDouble(inputPayload);
                existing.setMaxPayload(newPayload);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for payload. Keeping existing value.");
            }
        }

        // Battery Life (numeric)
        System.out.print("Enter new Battery Life [" + existing.getBatteryLife() + "]: ");
        String inputBattery = scanner.nextLine();
        if (!inputBattery.isEmpty()) {
            try {
                double newBatteryLife = Double.parseDouble(inputBattery);
                existing.setBatteryLife(newBatteryLife);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for battery life. Keeping existing value.");
            }
        }

        // Status
        System.out.print("Enter new Status [" + existing.getStatus() + "]: ");
        String newStatus = scanner.nextLine();
        if (!newStatus.isEmpty()) {
            existing.setStatus(newStatus);
        }

        // Location
        System.out.print("Enter new Location [" + existing.getLocation() + "]: ");
        String newLocation = scanner.nextLine();
        if (!newLocation.isEmpty()) {
            existing.setLocation(newLocation);
        }

        System.out.println("Drone updated.");
    }

    // Delete
    private static void deleteDrone() {
        System.out.print("Enter the Serial Number to delete: ");
        String serialNum = scanner.nextLine();

        if (DroneManager.removeDrone(serialNum)) {
            System.out.println("Drone deleted.");
        } else {
            System.out.println("No drone found with Serial Number: " + serialNum);
        }
    }

    // Search
    private static void searchDrone() {
        System.out.print("Enter Serial Number to search: ");
        String serialNum = scanner.nextLine();
        Drone found = DroneManager.findBySerialNumber(serialNum);
        if (found != null) {
            System.out.println("Drone found: " + found);
        } else {
            System.out.println("No drone found with Serial Number: " + serialNum);
        }
    }

    /*
     *  EQUIPMENT MENU
     */
    private static void equipmentMenu() {
        boolean backToMain = false;
        while (!backToMain) {
            switch (showEquipmentMenu()) {
                case 1:
                    addEquipment();
                    break;
                case 2:
                    editEquipment();
                    break;
                case 3:
                    deleteEquipment();
                    break;
                case 4:
                    searchEquipment();
                    break;
                case 5:
                    EquipmentManager.listAll();
                    break;
                case 6:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    private static int showEquipmentMenu() {
        System.out.println("\n=== Equipment Menu ===");
        System.out.println("1 - Add equipment");
        System.out.println("2 - Edit equipment");
        System.out.println("3 - Delete equipment");
        System.out.println("4 - Search equipment");
        System.out.println("5 - List all equipment");
        System.out.println("6 - Back to main menu");
        System.out.print("Enter choice: ");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } else {
            scanner.nextLine();
            return -1;
        }
    }

    // Add equipment
    private static void addEquipment() {
        System.out.print("Enter Serial Number: ");
        String serialNumber = scanner.nextLine();

        System.out.print("Enter Type: ");
        String type = scanner.nextLine();

        System.out.print("Enter Model: ");
        String equipModel = scanner.nextLine();

        System.out.print("Enter Dimensions: ");
        String dimensions = scanner.nextLine();

        System.out.print("Enter Weight (numeric): ");
        double weight = 0.0;
        try {
            weight = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for weight, defaulting to 0.0");
        }

        System.out.print("Enter Year (integer): ");
        int year = 0;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for year, defaulting to 0");
        }

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Location: ");
        String location = scanner.nextLine();

        Equipment newEquipment = new Equipment(serialNumber, type, equipModel,
                dimensions, weight, year,
                description, location);

        EquipmentManager.addEquipment(newEquipment);
    }

    // Edit equipment
    private static void editEquipment() {
        System.out.print("Enter the serial number of equipment to edit: ");
        String serialNumber = scanner.nextLine();
        Equipment existing = EquipmentManager.findBySerialNumber(serialNumber);
        if (existing == null) {
            System.out.println("No equipment found with that serial number.");
            return;
        }

        System.out.println("Leave blank to keep existing data.");

        // Type
        System.out.print("Enter new type [" + existing.getType() + "]: ");
        String newType = scanner.nextLine();
        if (!newType.isEmpty()) {
            existing.setType(newType);
        }

        // Equipment model
        System.out.print("Enter new model [" + existing.getEquipModel() + "]: ");
        String newModel = scanner.nextLine();
        if (!newModel.isEmpty()) {
            existing.setEquipModel(newModel);
        }

        // Dimensions
        System.out.print("Enter new dimensions [" + existing.getDimensions() + "]: ");
        String newDimensions = scanner.nextLine();
        if (!newDimensions.isEmpty()) {
            existing.setDimensions(newDimensions);
        }

        // Weight (numeric)
        System.out.print("Enter new weight [" + existing.getWeight() + "]: ");
        String newWeight = scanner.nextLine();
        if (!newWeight.isEmpty()) {
            try {
                existing.setWeight(Double.parseDouble(newWeight));
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Keeping existing weight.");
            }
        }

        // Year (numeric)
        System.out.print("Enter new year [" + existing.getYear() + "]: ");
        String newYear = scanner.nextLine();
        if (!newYear.isEmpty()) {
            try {
                existing.setYear(Integer.parseInt(newYear));
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Keeping existing year.");
            }
        }

        // Description
        System.out.print("Enter new description [" + existing.getDescription() + "]: ");
        String newDescription = scanner.nextLine();
        if (!newDescription.isEmpty()) {
            existing.setDescription(newDescription);
        }

        // Location
        System.out.print("Enter new location [" + existing.getLocation() + "]: ");
        String newLocation = scanner.nextLine();
        if (!newLocation.isEmpty()) {
            existing.setLocation(newLocation);
        }

        System.out.println("Equipment updated.");
    }


    // Delete equipment
    private static void deleteEquipment() {
        System.out.print("Enter the serial number of equipment to delete: ");
        String serialNumber = scanner.nextLine();
        if (EquipmentManager.removeEquipment(serialNumber)) {
            System.out.println("Equipment deleted.");
        } else {
            System.out.println("No equipment found with that serial number.");
        }
    }

    // Search
    private static void searchEquipment() {
        System.out.print("Enter the serial number to search: ");
        String serialNumber = scanner.nextLine();
        Equipment found = EquipmentManager.findBySerialNumber(serialNumber);
        if (found != null) {
            System.out.println("Found: " + found);
        } else {
            System.out.println("No equipment found with that serial number.");
        }
    }
}
