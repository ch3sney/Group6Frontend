import java.util.ArrayList;
import java.util.List;

public class DroneManager {
    // Static list for checkpoint 2
    private static final List<Drone> droneList = new ArrayList<>();

    // Create
    public static void addDrone(Drone drone) {
        droneList.add(drone);
        System.out.println("Drone added: " + drone);
    }

    // Search by serial number
    public static Drone findBySerialNumber(String serialNumber) {
        for (Drone d : droneList) {
            if (d.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                return d;
            }
        }
        return null; // not found
    }

    // Update
    public static boolean updateDrone(String droneId, Drone newData) {
        Drone existing = findBySerialNumber(droneId);
        if (existing == null) {
            return false;
        }
        existing.setModel(newData.getModel());
        existing.setMaxPayload(newData.getMaxPayload());
        existing.setBatteryLife(newData.getBatteryLife());
        existing.setStatus(newData.getStatus());
        existing.setLocation(newData.getLocation());
        return true;
    }

    // Delete
    public static boolean removeDrone(String droneId) {
        Drone d = findBySerialNumber(droneId);
        if (d != null) {
            droneList.remove(d);
            return true;
        }
        return false;
    }

    // Read
    public static void listAll() {
        if (droneList.isEmpty()) {
            System.out.println("No drone records found.");
        } else {
            for (Drone d : droneList) {
                System.out.println(d);
            }
        }
    }
}
