import java.util.ArrayList;
import java.util.List;

public class EquipmentManager {
    // Static list for checkpoint 2
    private static final List<Equipment> equipmentList = new ArrayList<>();

    // Create
    public static void addEquipment(Equipment equipment) {
        equipmentList.add(equipment);
        System.out.println("Equipment added: " + equipment);
    }

    // Search by serial number
    public static Equipment findBySerialNumber(String serialNumber) {
        for (Equipment e : equipmentList) {
            if (e.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                return e;
            }
        }
        return null; // not found
    }

    // Update
    public static boolean updateEquipment(String serialNumber, Equipment newData) {
        Equipment existing = findBySerialNumber(serialNumber);
        if (existing == null) {
            return false;
        }
        existing.setType(newData.getType());
        existing.setEquipModel(newData.getEquipModel());
        existing.setDimensions(newData.getDimensions());
        existing.setWeight(newData.getWeight());
        existing.setYear(newData.getYear());
        existing.setDescription(newData.getDescription());
        existing.setLocation(newData.getLocation());
        return true;
    }

    // Delete
    public static boolean removeEquipment(String serialNumber) {
        Equipment e = findBySerialNumber(serialNumber);
        if (e != null) {
            equipmentList.remove(e);
            return true;
        }
        return false;
    }

    // Read
    public static void listAll() {
        if (equipmentList.isEmpty()) {
            System.out.println("No equipment records found.");
        } else {
            for (Equipment e : equipmentList) {
                System.out.println(e);
            }
        }
    }
}
