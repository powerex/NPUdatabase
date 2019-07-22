import java.sql.*;
//import java.sql.Connection;
import java.util.Scanner;

public class SimpleConnectApplication {

    public static void main(String[] args) throws SQLException {

        final Scanner scanner = new Scanner(System.in);

        final String user = "root";
        final String password = "!NPUpassword2019";
//        final String url = "jdbc:mysql://localhost:3306/npu";

        final String url = "jdbc:mysql://localhost:3306/npu"+
                "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";


        final String query = "SELECT * FROM users WHERE id = (?)";
//        final String query = "SELECT * FROM users";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            final int index = scanner.nextInt();
            statement.setInt(1, index);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String byName = "login: " + resultSet.getString("login");
                String byIndex = "password: " + resultSet.getString(7);
                final int role = resultSet.getInt("role_id");
                System.out.println(byName);
                System.out.println(byIndex);
                System.out.println("role: " + role);
            }
        }
    }

}
