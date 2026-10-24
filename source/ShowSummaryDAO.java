import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowSummaryDAO {

    public void showSummary() {

        String sql =
                "SELECT "
                + "(SELECT COUNT(*) FROM books) AS total_books, "
                + "(SELECT COUNT(*) FROM members) AS total_members, "
                + "(SELECT COALESCE(SUM(total_copies), 0) FROM books) AS total_copies, "
                + "(SELECT COALESCE(SUM(available_copies), 0) FROM books) AS available_copies, "
                + "(SELECT COUNT(*) FROM book_transactions "
                + " WHERE return_date IS NULL) AS borrowed_books, "
                + "(SELECT COUNT(*) FROM book_transactions "
                + " WHERE return_date IS NOT NULL) AS returned_books, "
                + "(SELECT COALESCE(SUM(fine), 0) FROM book_transactions) AS total_fines";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                System.out.println("\n==================================");
                System.out.println("       BOOKNEST LIBRARY SUMMARY");
                System.out.println("==================================");

                System.out.println("Total Book Titles       : "
                        + rs.getInt("total_books"));

                System.out.println("Total Members           : "
                        + rs.getInt("total_members"));

                System.out.println("Total Book Copies       : "
                        + rs.getInt("total_copies"));

                System.out.println("Available Copies        : "
                        + rs.getInt("available_copies"));

                System.out.println("Currently Borrowed      : "
                        + rs.getInt("borrowed_books"));

                System.out.println("Returned Books          : "
                        + rs.getInt("returned_books"));

                System.out.println("Total Fines Recorded    : Rs. "
                        + rs.getBigDecimal("total_fines"));

                System.out.println("==================================");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error displaying summary: " + e.getMessage());
        }
    }
}