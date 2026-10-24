import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowAllBooksDAO {

    public void showAllBooks() {

        String sql =
                "SELECT book_id, title, author, total_copies, available_copies "
                + "FROM books ORDER BY book_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n========== ALL BOOKS ==========");

            while (rs.next()) {

                found = true;

                System.out.println("-------------------------------");
                System.out.println("Book ID: " + rs.getString("book_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author: " + rs.getString("author"));
                System.out.println("Total Copies: "
                        + rs.getInt("total_copies"));
                System.out.println("Available Copies: "
                        + rs.getInt("available_copies"));
            }

            if (!found) {
                System.out.println("No books available in the library.");
            }

            System.out.println("================================");

        } catch (SQLException e) {
            System.out.println(
                    "Error displaying books: " + e.getMessage());
        }
    }
}