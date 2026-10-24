import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterMemberDAO {

    public void registerMember(Member member) {

        String sql = "INSERT INTO members (member_id, name) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getMemberId());
            ps.setString(2, member.getName());

            ps.executeUpdate();

            System.out.println("Member registered successfully!");

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                System.out.println("Member ID already exists!");
            } else {
                System.out.println(
                        "Error registering member: " + e.getMessage());
            }
        }
    }
}