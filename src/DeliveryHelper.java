import java.sql.*;
import java.time.LocalDate;

public class DeliveryHelper {

    public static boolean scheduleDropoff(int memberId, String equipmentSerial, String droneSerial, LocalDate dropDate) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            if (!exists(conn, "SELECT 1 FROM Member WHERE MemberID = ?", memberId)) {
                System.out.println("No such member.");
                conn.rollback();
                return false;
            }
            if (!exists(conn, "SELECT 1 FROM Equipment WHERE Serial = ? AND Status = 'Rented' AND RenterID = ?", equipmentSerial, memberId)) {
                System.out.println("That equipment is not rented by this member.");
                conn.rollback();
                return false;
            }

            if (!isDroneAvailable(conn, droneSerial)) {
                System.out.println("Drone is not available.");
                conn.rollback();
                return false;
            }

            boolean already = exists(conn, "SELECT 1 FROM Delivery WHERE Customer = ?", memberId);
            if (already) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Delivery SET DropoffDate = ?, PickupDate = NULL, DroneSerial = ? " + "WHERE Customer = ?")) {
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

            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE Drone SET Status='In Use' WHERE Serial = ?")) {
                ps1.setString(1, droneSerial);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE Equipment SET DroneID = ? WHERE Serial = ?")) {
                ps2.setString(1, droneSerial);
                ps2.setString(2, equipmentSerial);
                ps2.executeUpdate();
            }

            // Use conn.commit() to lower amount of commits to DB and ensure no errors
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

            if (!exists(conn, "SELECT 1 FROM Delivery WHERE Customer = ?", memberId)) {
                System.out.println("No delivery scheduled for that member.");
                conn.rollback();
                return false;
            }

            if (!isDroneAvailable(conn, droneSerial)) {
                System.out.println("Drone is not available.");
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE Delivery SET PickupDate = ?, DroneSerial = ? WHERE Customer = ?")) {
                ps.setString(1, pickupDate.toString());
                ps.setString(2, droneSerial);
                ps.setInt(3, memberId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE Drone SET Status = 'In Use' WHERE Serial = ?")) {
                ps1.setString(1, droneSerial);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE Equipment SET DroneID = ? WHERE Serial = ?")) {
                ps2.setString(1, droneSerial);
                ps2.setString(2, equipmentSerial);
                ps2.executeUpdate();
            }

            // Use conn.commit() to lower amount of commits to DB and ensure no errors
            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean exists(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean isDroneAvailable(Connection c, String serial) throws SQLException {
        return exists(c, "SELECT 1 FROM Drone WHERE Serial = ? AND Status = 'Available'", serial);
    }
}
