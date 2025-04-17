import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== Drone Delivery Manager ===");

        while (running) {
            int choice = menu();
            switch (choice) {
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
                    reportsMenu();
                    break;
                case 8:
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
        String[] options = {"1 - Rent equipment", "2 - Return equipment", "3 - Deliver equipment", "4 - Pick up equipment", "5 - Drone manager", "6 - Equipment manager", "7 - Useful reports", "8 - Exit"};

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
        System.out.print("Serial number of equipment to rent: ");
        String serial = scanner.nextLine().trim();

        System.out.print("Member ID of renter: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid member‑ID.");
            return;
        }

        System.out.print("Cost per day (e.g. 19.99): ");
        double costPerDay;
        try {
            costPerDay = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        int rentalId = RentalHelper.checkout(serial, memberId, costPerDay);
        if (rentalId != -1) System.out.println("Rental created with RentalID = " + rentalId);
    }

    private static void returnEquipment() {
        System.out.print("Serial number being returned: ");
        String serial = scanner.nextLine().trim();

        if (RentalHelper.checkin(serial)) System.out.println("Return processed.");
    }

    private static void deliverEquipment() {

        System.out.print("Member ID to deliver to: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Bad number.");
            return;
        }

        System.out.print("Serial number of equipment being delivered: ");
        String equipSerial = scanner.nextLine().trim();

        /* List available drones */
        List<String> drones = DroneManager.listAvailableDrones();
        if (drones.isEmpty()) {
            System.out.println("No drones available.");
            return;
        }

        System.out.println("Available drones: " + drones);
        System.out.print("Enter drone serial to use (leave blank for first): ");
        String drone = scanner.nextLine().trim();
        if (drone.isEmpty()) drone = drones.get(0);
        if (!drones.contains(drone)) {
            System.out.println("Not in the list.");
            return;
        }

        System.out.print("Drop‑off date (YYYY‑MM‑DD) or blank for today: ");
        String dateIn = scanner.nextLine().trim();

        LocalDate dropDate;
        if (dateIn.isEmpty()) {
            dropDate = LocalDate.now();
        } else {
            dropDate = LocalDate.parse(dateIn);
        }

        if (DeliveryHelper.scheduleDropoff(memberId, equipSerial, drone, dropDate))
            System.out.println("Delivery scheduled.");
    }

    private static void pickupEquipment() {
        System.out.print("Member ID to pick up from: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Bad number.");
            return;
        }

        System.out.print("Serial number of equipment to pick up: ");
        String equipSerial = scanner.nextLine().trim();

        /* List available drones */
        List<String> drones = DroneManager.listAvailableDrones();
        if (drones.isEmpty()) {
            System.out.println("No drones available.");
            return;
        }

        System.out.println("Available drones: " + drones);
        System.out.print("Enter drone serial to use (leave blank for first): ");
        String drone = scanner.nextLine().trim();
        if (drone.isEmpty()) drone = drones.get(0);
        if (!drones.contains(drone)) {
            System.out.println("Not in the list.");
            return;
        }

        System.out.print("Pick‑up date (YYYY‑MM‑DD) or blank for today: ");
        String dateIn = scanner.nextLine().trim();
        LocalDate pickDate = dateIn.isEmpty() ? LocalDate.now() : LocalDate.parse(dateIn);

        if (DeliveryHelper.schedulePickup(memberId, equipSerial, drone, pickDate))
            System.out.println("Pick‑up scheduled.");
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
        // Prompt user for each field
        System.out.print("Enter Drone ID (Serial): ");
        String serial = scanner.nextLine();

        System.out.print("Enter Manufacturer ID (integer): ");
        int manufacturer = 0;
        try {
            manufacturer = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Manufacturer ID. Defaulting to 0.");
        }

        System.out.print("Enter Weight Capacity (numeric): ");
        double weightCapacity = 0.0;
        try {
            weightCapacity = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 0.0.");
        }

        System.out.print("Enter Year Manufactured (e.g. 2023): ");
        int yearManufactured = 0;
        try {
            yearManufactured = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Defaulting to 0.");
        }

        System.out.print("Enter Distance Autonomy (numeric): ");
        double distanceAutonomy = 0.0;
        try {
            distanceAutonomy = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 0.0.");
        }

        System.out.print("Enter Status (Available, In Use, Maintenance, Retired): ");
        String status = scanner.nextLine();

        System.out.print("Enter Max Speed (numeric): ");
        double maxSpeed = 0.0;
        try {
            maxSpeed = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Defaulting to 0.0.");
        }

        System.out.print("Enter Drone Name (optional): ");
        String name = scanner.nextLine();

        System.out.print("Enter Warranty Expiration (YYYY-MM-DD) or leave blank: ");
        String wExpInput = scanner.nextLine();
        LocalDate warrantyExpiration = null;
        if (!wExpInput.trim().isEmpty()) {
            try {
                warrantyExpiration = LocalDate.parse(wExpInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, ignoring warranty expiration.");
            }
        }

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter Current Location: ");
        String currentLocation = scanner.nextLine();

        System.out.print("Enter Warehouse Address (optional): ");
        String warehouseAddr = scanner.nextLine();

        // Create the new Drone object
        Drone newDrone = new Drone(serial, manufacturer, weightCapacity, yearManufactured, distanceAutonomy, status, maxSpeed, name, warrantyExpiration, model, currentLocation, warehouseAddr);

        // call the manager to insert into the DB
        DroneManager.addDrone(newDrone);
        System.out.println("Drone added successfully!");
    }

    // edit drone
    private static void editDrone() {
        System.out.print("Enter the Drone Serial (primary key) of the drone to edit: ");
        String serialNumber = scanner.nextLine();

        // Load existing record from DB
        Drone existing = DroneManager.findBySerialNumber(serialNumber);
        if (existing == null) {
            System.out.println("No drone found with that serial: " + serialNumber);
            return;
        }

        System.out.println("Leave any field blank to keep the existing value.");

        // Manufacturer (int)
        System.out.print("Manufacturer [" + existing.getManufacturer() + "]: ");
        String manufacturerInput = scanner.nextLine();
        int manufacturer = existing.getManufacturer();
        if (!manufacturerInput.isEmpty()) {
            try {
                manufacturer = Integer.parseInt(manufacturerInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping existing value.");
            }
        }

        // WeightCapacity (double)
        System.out.print("Weight Capacity [" + existing.getWeightCapacity() + "]: ");
        String weightCapInput = scanner.nextLine();
        double weightCapacity = existing.getWeightCapacity();
        if (!weightCapInput.isEmpty()) {
            try {
                weightCapacity = Double.parseDouble(weightCapInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping existing value.");
            }
        }

        // YearManufactured (int)
        System.out.print("Year Manufactured [" + existing.getYearManufactured() + "]: ");
        String yearInput = scanner.nextLine();
        int yearManufactured = existing.getYearManufactured();
        if (!yearInput.isEmpty()) {
            try {
                yearManufactured = Integer.parseInt(yearInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping existing value.");
            }
        }

        // DistanceAutonomy (double)
        System.out.print("Distance Autonomy [" + existing.getDistanceAutonomy() + "]: ");
        String distanceInput = scanner.nextLine();
        double distanceAutonomy = existing.getDistanceAutonomy();
        if (!distanceInput.isEmpty()) {
            try {
                distanceAutonomy = Double.parseDouble(distanceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping existing value.");
            }
        }

        // Status (String)
        System.out.print("Status [" + existing.getStatus() + "]: ");
        String statusInput = scanner.nextLine();
        String status = existing.getStatus();
        if (!statusInput.isEmpty()) {
            status = statusInput;
        }

        // MaxSpeed (double)
        System.out.print("Max Speed [" + existing.getMaxSpeed() + "]: ");
        String maxSpeedInput = scanner.nextLine();
        double maxSpeed = existing.getMaxSpeed();
        if (!maxSpeedInput.isEmpty()) {
            try {
                maxSpeed = Double.parseDouble(maxSpeedInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping existing value.");
            }
        }

        // Name (String)
        System.out.print("Drone Name [" + existing.getName() + "]: ");
        String nameInput = scanner.nextLine();
        String name = existing.getName();
        if (!nameInput.isEmpty()) {
            name = nameInput;
        }

        // WarrantyExpiration (LocalDate)
        System.out.print("Warranty Expiration (YYYY-MM-DD) [" + (existing.getWarrantyExpiration() != null ? existing.getWarrantyExpiration() : "null") + "]: ");
        String warrantyInput = scanner.nextLine();
        LocalDate warrantyExpiration = existing.getWarrantyExpiration();
        if (!warrantyInput.isEmpty()) {
            try {
                warrantyExpiration = LocalDate.parse(warrantyInput);
            } catch (Exception e) {
                System.out.println("Invalid date format. Keeping existing value.");
            }
        }

        // Model (String)
        System.out.print("Model [" + existing.getModel() + "]: ");
        String modelInput = scanner.nextLine();
        String model = existing.getModel();
        if (!modelInput.isEmpty()) {
            model = modelInput;
        }

        // CurrentLocation (String)
        System.out.print("Current Location [" + existing.getCurrentLocation() + "]: ");
        String locationInput = scanner.nextLine();
        String currentLocation = existing.getCurrentLocation();
        if (!locationInput.isEmpty()) {
            currentLocation = locationInput;
        }

        // WarehouseAddr (String)
        System.out.print("Warehouse Address [" + existing.getWarehouseAddr() + "]: ");
        String warehouseAddrInput = scanner.nextLine();
        String warehouseAddr = existing.getWarehouseAddr();
        if (!warehouseAddrInput.isEmpty()) {
            warehouseAddr = warehouseAddrInput;
        }

        // Create a new Drone with updated data (same serial #, updated fields)
        Drone updatedDrone = new Drone(existing.getSerial(),    // keep the same Serial (PK)
                manufacturer, weightCapacity, yearManufactured, distanceAutonomy, status, maxSpeed, name, warrantyExpiration, model, currentLocation, warehouseAddr);

        // Save changes in the DB
        boolean success = DroneManager.updateDrone(serialNumber, updatedDrone);
        if (success) {
            System.out.println("Drone updated successfully.");
        } else {
            System.out.println("Drone update failed (no rows affected).");
        }
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

        Equipment newEquipment = new Equipment(serialNumber, type, equipModel, dimensions, weight, year, description, location);

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

    // Useful reports menu
    private static void reportsMenu() {
        boolean exit = false;
        while (!exit) {
            switch (showReportsMenu()) {
                case 1:
                    UsefulReports.rentingCheckouts(scanner);
                    return;
                case 2:
                    UsefulReports.popularItem();
                    return;
                case 3:
                    UsefulReports.popularManufacturer();
                    return;
                case 4:
                    UsefulReports.popularDrone();
                    return;
                case 5:
                    UsefulReports.itemsCheckedOut();
                    return;
                case 6:
                    UsefulReports.equipmentByTypeBeforeYear(scanner);
                    return;
                case 7:
                    exit = true;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    // Show reports menu
    private static int showReportsMenu() {
        System.out.println("\n=== Useful reports ===");
        System.out.println("1 - Renting checkouts (by member)");
        System.out.println("2 - Popular item");
        System.out.println("3 - Popular manufacturer");
        System.out.println("4 - Popular drone");
        System.out.println("5 - Items checked‑out (top member)");
        System.out.println("6 - Equipment by type released before YEAR");
        System.out.println("7 - Back to main menu");
        System.out.print("Enter choice: ");

        if (scanner.hasNextInt()) {
            int c = scanner.nextInt();
            scanner.nextLine();
            return c;
        }
        scanner.nextLine();
        return -1;
    }

}
