import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class IssueBookDAO {

    public void issueBook(String memberId, String bookId) {

        String checkMember =
                "SELECT member_id FROM members WHERE member_id = ?";

        String checkBook =
                "SELECT available_copies FROM books "
                + "WHERE book_id = ? FOR UPDATE";

        String countBooks =
                "SELECT COUNT(*) FROM book_transactions "
                + "WHERE member_id = ? AND return_date IS NULL";

        String insertIssue =
                "INSERT INTO book_transactions "
                + "(member_id, book_id, issue_date, days_kept, fine) "
                + "VALUES (?, ?, ?, 0, 0.00)";

        String updateBook =
                "UPDATE books SET available_copies = available_copies - 1 "
                + "WHERE book_id = ? AND available_copies > 0";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Step 1: Check whether the member exists
            try (PreparedStatement ps = con.prepareStatement(checkMember)) {

                ps.setString(1, memberId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Member not found!");
                        con.rollback();
                        return;
                    }
                }
            }

            // Step 2: Check whether the book exists and is available
            try (PreparedStatement ps = con.prepareStatement(checkBook)) {

                ps.setString(1, bookId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Book not found!");
                        con.rollback();
                        return;
                    }

                    if (rs.getInt("available_copies") <= 0) {
                        System.out.println("No copies available!");
                        con.rollback();
                        return;
                    }
                }
            }

            // Step 3: Check the member's active borrowed-book count
            try (PreparedStatement ps = con.prepareStatement(countBooks)) {

                ps.setString(1, memberId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next() && rs.getInt(1) >= 3) {
                        System.out.println(
                                "A member cannot borrow more than 3 books!");
                        con.rollback();
                        return;
                    }
                }
            }

            // Step 4: Insert the issue transaction
            try (PreparedStatement ps = con.prepareStatement(insertIssue)) {

                ps.setString(1, memberId);
                ps.setString(2, bookId);
                ps.setDate(3, new Date(System.currentTimeMillis()));

                ps.executeUpdate();
            }

            // Step 5: Decrease available copies
            try (PreparedStatement ps = con.prepareStatement(updateBook)) {

                ps.setString(1, bookId);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    System.out.println("Unable to issue book!");
                    return;
                }
            }

            // Step 6: Save the transaction
            con.commit();

            System.out.println("\nBook issued successfully!");
            System.out.println("Member ID: " + memberId);
            System.out.println("Book ID: " + bookId);
            System.out.println("Issue Date: "
                    + new Date(System.currentTimeMillis()));
            System.out.println("Return the book within 7 days.");

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    System.out.println("Database rollback failed.");
                }
            }

            System.out.println("Error issuing book: " + e.getMessage());

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