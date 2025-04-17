import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public final class UsefulReports {

    // Amount of checkouts
    public static void rentingCheckouts(Scanner sc) {
        System.out.print("Enter member ID: ");
        int memberId = Integer.parseInt(sc.nextLine());

        String sql = """
            SELECT COUNT(DISTINCT e.Serial) AS total
            FROM   Equipment e
            JOIN   Rental    r ON r.RentalID = e.RentalID
            WHERE  r.MemberID = ?
            """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("Member #%d has rented %d item(s).%n",
                            memberId, rs.getInt("total"));
                } else {
                    System.out.println("No rentals found for that member.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Popular item
    public static void popularItem() {
        String sql = """
            WITH stats AS (
              SELECT e.Serial,
                     e.Model,
                     COUNT(*) AS times,
                     SUM(JULIANDAY(r.EndDate) - JULIANDAY(r.StartDate)) AS days
              FROM   Equipment e
              JOIN   Rental r ON r.RentalID = e.RentalID
              GROUP  BY e.Serial
            )
            SELECT * FROM stats
            ORDER  BY days DESC, times DESC
            LIMIT  1;
            """;
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("Most popular item: %s (model %s) – %d rentals, %.0f days total%n",
                        rs.getString("Serial"),
                        rs.getString("Model"),
                        rs.getInt("times"),
                        rs.getDouble("days"));
            } else {
                System.out.println("No rental data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Popular manufacturer
    public static void popularManufacturer() {
        String sql = """
        SELECT TimesRented, m.Name
        FROM  Manufacturer AS m,
              (SELECT COUNT(e.ManufacturerID) AS TimesRented,
                      e.ManufacturerID        AS manu_ID
               FROM   Equipment  AS e,
                      Rental     AS r
               WHERE  e.RentalID = r.RentalID
               GROUP  BY e.ManufacturerID
               ORDER  BY TimesRented DESC
               LIMIT  1)
        WHERE manu_ID = m.ManufacturerID;
        """;
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("Top manufacturer: %s – %d unit(s) rented%n",
                        rs.getString("Name"),
                        rs.getInt("TimesRented"));
            } else {
                System.out.println("No rental data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Popular drone
    public static void popularDrone() {
        String sql = """
            SELECT d.Serial,
                   d.Model,
                   COUNT(*) AS deliveries,
                   SUM(d.DistanceAutonomy) AS miles
            FROM   Delivery del
            JOIN   Drone d ON d.Serial = del.DroneSerial
            GROUP  BY d.Serial
            ORDER  BY deliveries DESC, miles DESC
            LIMIT  1;
            """;
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("Most‑used drone: %s (%s) – %d trips, %.1f mi flown%n",
                        rs.getString("Serial"),
                        rs.getString("Model"),
                        rs.getInt("deliveries"),
                        rs.getDouble("miles"));
            } else {
                System.out.println("No delivery data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Items checked‑out
    public static void itemsCheckedOut() {
        String sql = """
            SELECT r.MemberID,
                   mb.FirstName || ' ' || mb.LastName AS name,
                   COUNT(*) AS total
            FROM   Rental r
            JOIN   Member mb ON mb.MemberID = r.MemberID
            GROUP  BY r.MemberID
            ORDER  BY total DESC
            LIMIT  1;
            """;
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("Most active renter: %s (ID %d) – %d item(s)%n",
                        rs.getString("name"),
                        rs.getInt("MemberID"),
                        rs.getInt("total"));
            } else {
                System.out.println("No rental data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Equipment by type before a given year
    public static void equipmentByTypeBeforeYear(Scanner sc) {
        System.out.print("Enter equipment type: ");
        String type = sc.nextLine();
        System.out.print("Enter cutoff year: ");
        int year = Integer.parseInt(sc.nextLine());

        String sql = """
            SELECT Serial, Type, Year
            FROM   Equipment
            WHERE  Type = ? AND Year < ?
            ORDER  BY Year DESC;
            """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%n--- %s before %d ---%n", type, year);
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("* %s – %s (year %d)%n",
                            rs.getString("Serial"),
                            rs.getString("Description"),
                            rs.getInt("Year"));
                }
                if (!found) System.out.println("No matching equipment.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
