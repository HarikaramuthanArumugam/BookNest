import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBookDAO {

    // Search by Book ID
    public void searchBook(String bookId) {

        String sql =
                "SELECT book_id, title, author, total_copies, available_copies "
                + "FROM books WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println("\n===== Book Found =====");
                    System.out.println("Book ID: " + rs.getString("book_id"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Author: " + rs.getString("author"));
                    System.out.println("Total Copies: "
                            + rs.getInt("total_copies"));
                    System.out.println("Available Copies: "
                            + rs.getInt("available_copies"));

                } else {
                    System.out.println("Book not found!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching book: " + e.getMessage());
        }
    }

    // Search by Book Title
    public void searchBook(String title, boolean searchByTitle) {

        String sql =
                "SELECT book_id, title, author, total_copies, available_copies "
                + "FROM books WHERE title LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + title + "%");

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\n===== Search Results =====");

                while (rs.next()) {

                    found = true;

                    System.out.println("---------------------------");
                    System.out.println("Book ID: " + rs.getString("book_id"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Author: " + rs.getString("author"));
                    System.out.println("Total Copies: "
                            + rs.getInt("total_copies"));
                    System.out.println("Available Copies: "
                            + rs.getInt("available_copies"));
                }

                if (!found) {
                    System.out.println(
                            "No books found with that title!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching book: " + e.getMessage());
        }
    }
}