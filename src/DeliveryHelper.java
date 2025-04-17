import java.sql.*;
import java.time.LocalDate;

public class DeliveryHelper {
    public static boolean scheduleDropoff(int memberId, String equipmentSerial, String droneSerial, LocalDate dropDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Member WHERE MemberID = ?")) {
                ps.setInt(1, memberId);
                if (!ps.executeQuery().next()) {
                    System.out.println("No such member.");
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Equipment " + "WHERE Serial = ? AND Status = 'Rented' AND RenterID = ?")) {
                ps.setString(1, equipmentSerial);
                ps.setInt(2, memberId);
                if (!ps.executeQuery().next()) {
                    System.out.println("That equipment is not rented by this member.");
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Drone WHERE Serial = ? AND Status = 'Available'")) {
                ps.setString(1, droneSerial);
                if (!ps.executeQuery().next()) {
                    System.out.println("Drone is not available.");
                    conn.rollback();
                    return false;
                }
            }

            boolean alreadyScheduled;
            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Delivery WHERE Customer = ?")) {
                ps.setInt(1, memberId);
                alreadyScheduled = ps.executeQuery().next();
            }

            if (alreadyScheduled) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Delivery " + "SET DropoffDate = ?, PickupDate = NULL, DroneSerial = ? " + "WHERE Customer = ?")) {
                    ps.setString(1, dropDate.toString());
                    ps.setString(2, droneSerial);
                    ps.setInt(3, memberId);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Delivery(Customer, DropoffDate, PickupDate, DroneSerial, FulfilledBy) " + "VALUES(?, ?, NULL, ?, NULL)")) {
                    ps.setInt(1, memberId);
                    ps.setString(2, dropDate.toString());
                    ps.setString(3, droneSerial);
                    ps.executeUpdate();
                }
            }

            /* 5. Mark drone and equipment */
            try (PreparedStatement ps = conn.prepareStatement("UPDATE Drone SET Status = 'In Use' WHERE Serial = ?")) {
                ps.setString(1, droneSerial);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE Equipment SET DroneID = ? WHERE Serial = ?")) {
                ps.setString(1, droneSerial);
                ps.setString(2, equipmentSerial);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean schedulePickup(int memberId, String equipmentSerial, String droneSerial, LocalDate pickupDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Delivery WHERE Customer = ?")) {
                ps.setInt(1, memberId);
                if (!ps.executeQuery().next()) {
                    System.out.println("No delivery scheduled for member.");
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Drone WHERE Serial = ? AND Status = 'Available'")) {
                ps.setString(1, droneSerial);
                if (!ps.executeQuery().next()) {
                    System.out.println("Drone is not available.");
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE Delivery " + "SET PickupDate = ?, DroneSerial = ? " + "WHERE Customer = ?")) {
                ps.setString(1, pickupDate.toString());
                ps.setString(2, droneSerial);
                ps.setInt(3, memberId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE Drone SET Status = 'In Use' WHERE Serial = ?")) {
                ps.setString(1, droneSerial);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE Equipment SET DroneID = ? WHERE Serial = ?")) {
                ps.setString(1, droneSerial);
                ps.setString(2, equipmentSerial);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
