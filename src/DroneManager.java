import java.sql.*;
import java.time.LocalDate;

public class DroneManager {
    // Add drone
    public static void addDrone(Drone d) {
        final String sql = """
                    INSERT INTO Drone
                      (Serial, Manufacturer, WeightCapacity, YearManufactured,
                       DistanceAutonomy, Status, MaxSpeed, Name,
                       WarrantyExpiration, Model, CurrentLocation, WarehouseAddr)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getSerial());
            ps.setInt(2, d.getManufacturer());
            ps.setDouble(3, d.getWeightCapacity());
            ps.setInt(4, d.getYearManufactured());
            ps.setDouble(5, d.getDistanceAutonomy());
            ps.setString(6, d.getStatus());
            ps.setDouble(7, d.getMaxSpeed());
            ps.setString(8, d.getName());

            if (d.getWarrantyExpiration() != null) ps.setString(9, d.getWarrantyExpiration().toString());
            else ps.setNull(9, Types.VARCHAR);

            ps.setString(10, d.getModel());
            ps.setString(11, d.getCurrentLocation());
            ps.setString(12, d.getWarehouseAddr());

            ps.executeUpdate();
            System.out.println("Drone added: " + d.getSerial());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Find drone by serial number
    public static Drone findBySerialNumber(String serial) {
        final String sql = "SELECT * FROM Drone WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serial);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return toDrone(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Update drone entry
    public static boolean updateDrone(String serial, Drone d) {
        final String sql = """
                    UPDATE Drone SET
                        Manufacturer      = ?,  WeightCapacity   = ?,
                        YearManufactured  = ?,  DistanceAutonomy = ?,
                        Status            = ?,  MaxSpeed         = ?,
                        Name              = ?,  WarrantyExpiration = ?,
                        Model             = ?,  CurrentLocation  = ?,
                        WarehouseAddr     = ?
                    WHERE Serial = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getManufacturer());
            ps.setDouble(2, d.getWeightCapacity());
            ps.setInt(3, d.getYearManufactured());
            ps.setDouble(4, d.getDistanceAutonomy());
            ps.setString(5, d.getStatus());
            ps.setDouble(6, d.getMaxSpeed());
            ps.setString(7, d.getName());

            if (d.getWarrantyExpiration() != null) ps.setString(8, d.getWarrantyExpiration().toString());
            else ps.setNull(8, Types.VARCHAR);

            ps.setString(9, d.getModel());
            ps.setString(10, d.getCurrentLocation());
            ps.setString(11, d.getWarehouseAddr());
            ps.setString(12, serial);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Remove drone
    public static boolean removeDrone(String serial) {
        final String sql = "DELETE FROM Drone WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serial);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // List all drones
    public static void listAll() {
        final String sql = "SELECT * FROM Drone";
        try (Connection conn = DatabaseConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(toDrone(rs));
            }
            if (!found) System.out.println("No drone records found.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to create drone object
    private static Drone toDrone(ResultSet rs) throws SQLException {
        Drone d = new Drone();
        d.setSerial(rs.getString("Serial"));
        d.setManufacturer(rs.getInt("Manufacturer"));
        d.setWeightCapacity(rs.getDouble("WeightCapacity"));
        d.setYearManufactured(rs.getInt("YearManufactured"));
        d.setDistanceAutonomy(rs.getDouble("DistanceAutonomy"));
        d.setStatus(rs.getString("Status"));
        d.setMaxSpeed(rs.getDouble("MaxSpeed"));
        d.setName(rs.getString("Name"));

        String exp = rs.getString("WarrantyExpiration");
        if (exp != null) d.setWarrantyExpiration(LocalDate.parse(exp));

        d.setModel(rs.getString("Model"));
        d.setCurrentLocation(rs.getString("CurrentLocation"));
        d.setWarehouseAddr(rs.getString("WarehouseAddr"));
        return d;
    }

    // Return the serial numbers of available drones
    public static java.util.List<String> listAvailableDrones() {
        java.util.List<String> out = new java.util.ArrayList<>();
        final String sql = "SELECT Serial FROM Drone WHERE Status = 'Available'";

        try (Connection conn = DatabaseConnection.getConnection(); java.sql.Statement st = conn.createStatement(); java.sql.ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) out.add(rs.getString(1));

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

}
