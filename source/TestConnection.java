import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection()) {

            if (con.isValid(5)) {
                System.out.println("Connected to MySQL successfully!");
            } else {
                System.out.println("Connection is not valid!");
            }

        } catch (Exception e) {

            System.out.println("Connection failed!");
            e.printStackTrace();

        }
    }
}