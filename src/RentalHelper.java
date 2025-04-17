import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalHelper {
    public static int checkout(String equipSerial,
                               int memberId,
                               double costPerDay) {
        final String Equip_Query = "SELECT Status FROM Equipment WHERE Serial = ?";
        final String Insert_Rental  = "INSERT INTO Rental " +
                "(StartDate, EndDate, Fee, CostPerDay, Status, MemberID) " +
                "VALUES( ?, NULL, NULL, ?, 'Active', ? )";
        final String Update_Equip = "UPDATE Equipment SET " +
                "Status = 'Rented', RenterID = ?, RentalID = ? " +
                "WHERE Serial = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psEquip = conn.prepareStatement(Equip_Query)) {
                psEquip.setString(1, equipSerial);
                try (ResultSet rs = psEquip.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No equipment with that serial.");
                        conn.rollback();
                        return -1;
                    }
                    if (!"Available".equalsIgnoreCase(rs.getString("Status"))) {
                        System.out.println("Item is not available.");
                        conn.rollback();
                        return -1;
                    }
                }
            }

            int rentalId;
            try (PreparedStatement psRent = conn.prepareStatement(Insert_Rental, Statement.RETURN_GENERATED_KEYS)) {
                psRent.setString(1, LocalDate.now().toString());
                psRent.setDouble(2, costPerDay);
                psRent.setInt   (3, memberId);
                psRent.executeUpdate();

                try (ResultSet keys = psRent.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    rentalId = keys.getInt(1);
                }
            }

            try (PreparedStatement psUpd = conn.prepareStatement(Update_Equip)) {
                psUpd.setInt   (1, memberId);
                psUpd.setInt   (2, rentalId);
                psUpd.setString(3, equipSerial);
                psUpd.executeUpdate();
            }

            // Use conn.commit() to lower amount of commits to DB and ensure no errors
            conn.commit();
            return rentalId;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static boolean checkin(String equipSerial) {
        final String Query_Equip = "SELECT RentalID FROM Equipment " +
                "WHERE Serial = ? AND Status = 'Rented'";
        final String Query_Rental  = "SELECT StartDate, CostPerDay FROM Rental WHERE RentalID = ?";
        final String Update_Rental  = "UPDATE Rental SET EndDate = ?, Fee = ?, Status = 'Completed' " +
                "WHERE RentalID = ?";
        final String Update_Equip = "UPDATE Equipment SET " +
                "Status = 'Available', RenterID = NULL, RentalID = NULL " +
                "WHERE Serial = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            int rentalId;
            try (PreparedStatement psEq = conn.prepareStatement(Query_Equip)) {
                psEq.setString(1, equipSerial);
                try (ResultSet rs = psEq.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("That item is not out on rental.");
                        conn.rollback();
                        return false;
                    }
                    rentalId = rs.getInt("RentalID");
                }
            }

            LocalDate start;
            double costPerDay;
            try (PreparedStatement psRent = conn.prepareStatement(Query_Rental)) {
                psRent.setInt(1, rentalId);
                try (ResultSet rs = psRent.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    start = LocalDate.parse(rs.getString("StartDate"));
                    costPerDay = rs.getDouble("CostPerDay");
                }
            }
            long days = Math.max(1, ChronoUnit.DAYS.between(start, LocalDate.now()));
            double fee = days * costPerDay;

            try (PreparedStatement psUpdRent = conn.prepareStatement(Update_Rental)) {
                psUpdRent.setString(1, LocalDate.now().toString());
                psUpdRent.setDouble(2, fee);
                psUpdRent.setInt(3, rentalId);
                psUpdRent.executeUpdate();
            }

            try (PreparedStatement psUpdEq = conn.prepareStatement(Update_Equip)) {
                psUpdEq.setString(1, equipSerial);
                psUpdEq.executeUpdate();
            }

            // Use conn.commit() to lower amount of commits to DB and ensure no errors
            conn.commit();
            System.out.printf("Return complete. Rental fee = $%.2f%n", fee);
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
