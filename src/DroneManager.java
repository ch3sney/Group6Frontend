import java.sql.*;
import java.time.LocalDate;

public class DroneManager {

    // CREATE (INSERT)
    public static void addDrone(Drone drone) {
        String sql = "INSERT INTO Drone (Serial, Manufacturer, WeightCapacity, YearManufactured, DistanceAutonomy, "
                + "Status, MaxSpeed, Name, WarrantyExpiration, Model, CurrentLocation, WarehouseAddr) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,  drone.getSerial());
            pstmt.setInt(2,    drone.getManufacturer());
            pstmt.setDouble(3, drone.getWeightCapacity());
            pstmt.setInt(4,    drone.getYearManufactured());
            pstmt.setDouble(5, drone.getDistanceAutonomy());
            pstmt.setString(6, drone.getStatus());
            pstmt.setDouble(7, drone.getMaxSpeed());
            pstmt.setString(8, drone.getName());
            if (drone.getWarrantyExpiration() != null) {
                pstmt.setString(9, drone.getWarrantyExpiration().toString());
            } else {
                pstmt.setNull(9, Types.VARCHAR);
            }

            pstmt.setString(10, drone.getModel());
            pstmt.setString(11, drone.getCurrentLocation());
            pstmt.setString(12, drone.getWarehouseAddr());

            pstmt.executeUpdate();
            System.out.println("Drone added: " + drone.getSerial());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (SELECT by Serial)
    public static Drone findBySerialNumber(String serial) {
        String sql = "SELECT * FROM Drone WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serial);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Drone drone = new Drone();
                    drone.setSerial(rs.getString("Serial"));
                    drone.setManufacturer(rs.getInt("Manufacturer"));
                    drone.setWeightCapacity(rs.getDouble("WeightCapacity"));
                    drone.setYearManufactured(rs.getInt("YearManufactured"));
                    drone.setDistanceAutonomy(rs.getDouble("DistanceAutonomy"));
                    drone.setStatus(rs.getString("Status"));
                    drone.setMaxSpeed(rs.getDouble("MaxSpeed"));
                    drone.setName(rs.getString("Name"));

                    String wExp = rs.getString("WarrantyExpiration");
                    if (wExp != null) {
                        drone.setWarrantyExpiration(LocalDate.parse(wExp));
                    }
                    drone.setModel(rs.getString("Model"));
                    drone.setCurrentLocation(rs.getString("CurrentLocation"));
                    drone.setWarehouseAddr(rs.getString("WarehouseAddr"));
                    return drone;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Not found or error
    }

    // UPDATE
    public static boolean updateDrone(String serial, Drone newData) {
        String sql = "UPDATE Drone SET "
                + "Manufacturer = ?, WeightCapacity = ?, YearManufactured = ?, DistanceAutonomy = ?, "
                + "Status = ?, MaxSpeed = ?, Name = ?, WarrantyExpiration = ?, Model = ?, "
                + "CurrentLocation = ?, WarehouseAddr = ? "
                + "WHERE Serial = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,    newData.getManufacturer());
            pstmt.setDouble(2, newData.getWeightCapacity());
            pstmt.setInt(3,    newData.getYearManufactured());
            pstmt.setDouble(4, newData.getDistanceAutonomy());
            pstmt.setString(5, newData.getStatus());
            pstmt.setDouble(6, newData.getMaxSpeed());
            pstmt.setString(7, newData.getName());

            if (newData.getWarrantyExpiration() != null) {
                pstmt.setString(8, newData.getWarrantyExpiration().toString());
            } else {
                pstmt.setNull(8, Types.VARCHAR);
            }

            pstmt.setString(9,  newData.getModel());
            pstmt.setString(10, newData.getCurrentLocation());
            pstmt.setString(11, newData.getWarehouseAddr());
            pstmt.setString(12, serial);

            int rowsAffected = pstmt.executeUpdate();
            return (rowsAffected > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public static boolean removeDrone(String serial) {
        String sql = "DELETE FROM Drone WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serial);
            int rowsAffected = pstmt.executeUpdate();
            return (rowsAffected > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // LIST ALL DRONES
    public static void listAll() {
        String sql = "SELECT * FROM Drone";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean foundAny = false;
            while (rs.next()) {
                foundAny = true;
                Drone drone = new Drone();

                drone.setSerial(rs.getString("Serial"));
                drone.setManufacturer(rs.getInt("Manufacturer"));
                drone.setWeightCapacity(rs.getDouble("WeightCapacity"));
                drone.setYearManufactured(rs.getInt("YearManufactured"));
                drone.setDistanceAutonomy(rs.getDouble("DistanceAutonomy"));
                drone.setStatus(rs.getString("Status"));
                drone.setMaxSpeed(rs.getDouble("MaxSpeed"));
                drone.setName(rs.getString("Name"));

                String wExp = rs.getString("WarrantyExpiration");
                if (wExp != null) {
                    drone.setWarrantyExpiration(LocalDate.parse(wExp));
                }
                drone.setModel(rs.getString("Model"));
                drone.setCurrentLocation(rs.getString("CurrentLocation"));
                drone.setWarehouseAddr(rs.getString("WarehouseAddr"));

                System.out.println(drone);
            }

            if (!foundAny) {
                System.out.println("No drone records found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
