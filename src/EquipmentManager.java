import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentManager {

    // Add equipment
    public static void addEquipment(Equipment e) {
        final String sql = """
                    INSERT INTO Equipment
                      (Serial, Type, Model, Dimensions, Weight, Year, Status, Location)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getSerialNumber());
            ps.setString(2, e.getType());
            ps.setString(3, e.getEquipModel());
            ps.setString(4, e.getDimensions());
            ps.setDouble(5, e.getWeight());
            ps.setInt(6, e.getYear());
            ps.setString(7, (e.getDescription() == null || e.getDescription().isBlank()) ? "AVAILABLE" : e.getDescription());
            ps.setString(8, e.getLocation());

            ps.executeUpdate();
            System.out.println("Equipment added: " + e.getSerialNumber());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Find equipment
    public static Equipment findBySerialNumber(String serial) {
        final String sql = "SELECT * FROM Equipment WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serial);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toEquipment(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Update equipent
    public static boolean updateEquipment(String serial, Equipment newData) {
        final String sql = """
                    UPDATE Equipment SET
                        Type        = ?,
                        Model       = ?,
                        Dimensions  = ?,
                        Weight      = ?,
                        Year        = ?,
                        Status      = ?,
                        Location    = ?
                    WHERE Serial = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newData.getType());
            ps.setString(2, newData.getEquipModel());
            ps.setString(3, newData.getDimensions());
            ps.setDouble(4, newData.getWeight());
            ps.setInt(5, newData.getYear());
            ps.setString(6, newData.getDescription());
            ps.setString(7, newData.getLocation());
            ps.setString(8, serial);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Delete equipment
    public static boolean removeEquipment(String serial) {
        final String sql = "DELETE FROM Equipment WHERE Serial = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serial);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // List all equipment
    public static void listAll() {
        final String sql = "SELECT * FROM Equipment";
        try (Connection conn = DatabaseConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.println(toEquipment(rs));
            }
            if (!any) System.out.println("No equipment records found.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to create equipment object
    private static Equipment toEquipment(ResultSet rs) throws SQLException {
        Equipment e = new Equipment();
        e.setSerialNumber(rs.getString("Serial"));
        e.setType(rs.getString("Type"));
        e.setEquipModel(rs.getString("Model"));
        e.setDimensions(rs.getString("Dimensions"));
        e.setWeight(rs.getDouble("Weight"));
        e.setYear(rs.getInt("Year"));
        e.setDescription(rs.getString("Status"));
        e.setLocation(rs.getString("Location"));
        return e;
    }
}
