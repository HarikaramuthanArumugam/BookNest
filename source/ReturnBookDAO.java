import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;

public class ReturnBookDAO {

    public void returnBook(String memberId, String bookId) {

        String findIssue =
                "SELECT issue_id, issue_date FROM book_transactions "
                + "WHERE member_id = ? AND book_id = ? "
                + "AND return_date IS NULL "
                + "ORDER BY issue_date ASC, issue_id ASC "
                + "LIMIT 1 FOR UPDATE";

        String updateTransaction =
                "UPDATE book_transactions "
                + "SET return_date = ?, days_kept = ?, fine = ? "
                + "WHERE issue_id = ? AND return_date IS NULL";

        String updateBook =
                "UPDATE books "
                + "SET available_copies = available_copies + 1 "
                + "WHERE book_id = ? "
                + "AND available_copies < total_copies";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int issueId;
            LocalDate issueDate;

            // Step 1: Find the active issue
            try (PreparedStatement ps = con.prepareStatement(findIssue)) {

                ps.setString(1, memberId);
                ps.setString(2, bookId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println(
                                "No active issue found for this member and book!");
                        con.rollback();
                        return;
                    }

                    issueId = rs.getInt("issue_id");
                    issueDate = rs.getDate("issue_date").toLocalDate();
                }
            }

            // Step 2: Calculate the number of days kept
            LocalDate today = LocalDate.now();

            long days = ChronoUnit.DAYS.between(issueDate, today);

            if (days < 0) {
                con.rollback();
                System.out.println("Invalid issue date!");
                return;
            }

            int daysKept = Math.toIntExact(days);

            // Step 3: Calculate the fine: Rs. 2 per day after 7 days
            long extraDays = Math.max(0, days - 7);

            BigDecimal fine = BigDecimal.valueOf(extraDays)
                    .multiply(BigDecimal.valueOf(2));

            // Step 4: Update the transaction
            try (PreparedStatement ps =
                         con.prepareStatement(updateTransaction)) {

                ps.setDate(1, Date.valueOf(today));
                ps.setInt(2, daysKept);
                ps.setBigDecimal(3, fine);
                ps.setInt(4, issueId);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    System.out.println("Unable to return book!");
                    return;
                }
            }

            // Step 5: Increase available copies
            try (PreparedStatement ps = con.prepareStatement(updateBook)) {

                ps.setString(1, bookId);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    System.out.println(
                            "Unable to update book availability!");
                    return;
                }
            }

            // Step 6: Commit the transaction
            con.commit();

            System.out.println("\nBook returned successfully!");
            System.out.println("Member ID: " + memberId);
            System.out.println("Book ID: " + bookId);
            System.out.println("Days Kept: " + daysKept);
            System.out.println("Fine: Rs. " + fine);

        } catch (SQLException | ArithmeticException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    System.out.println("Database rollback failed.");
                }
            }

            System.out.println("Error returning book: " + e.getMessage());

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing database connection.");
                }
            }
        }
    }
}