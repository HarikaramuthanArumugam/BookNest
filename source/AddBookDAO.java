import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookDAO {

    public void addBook(Book book) {
        String sql = "INSERT INTO books " +
                "(book_id, title, author, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setInt(4, book.getTotalCopies());
            ps.setInt(5, book.getAvailableCopies());

            ps.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            System.out.println("Unable to add book: " + e.getMessage());
        }
    }
}
