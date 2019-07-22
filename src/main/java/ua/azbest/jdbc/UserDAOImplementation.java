package ua.azbest.jdbc;

import com.sun.istack.internal.NotNull;
import ua.azbest.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImplementation implements DAO<User, String> {

    @NotNull
    private final Connection connection;

    public UserDAOImplementation(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(@NotNull final User model) {
        int result = 0;

        try (PreparedStatement statement = connection.prepareStatement(SQLUser.INSERT.QUERY)) {
            statement.setString(1, model.getName());
            statement.setString(2, model.getSurname());
            statement.setInt(3, model.getAge());
            statement.setInt(4,model.getRole().getId());
            statement.setString(5, model.getLogin());
            statement.setString(6, model.getPassword());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User read(String name) {
        final User result = new User();
        result.setId(-1);

        try (PreparedStatement statement = connection.prepareStatement(SQLUser.GET.QUERY)) {
            statement.setString(1, name);
            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result.setId(Integer.parseInt(rs.getString("id")));
                result.setName(name);
                result.setSurname(rs.getString("surname"));
                result.setAge(rs.getInt("age"));
                result.setLogin(rs.getString("login"));
                result.setPassword(rs.getString("password"));
                result.setRole(new User.Role(rs.getInt("role_id"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(User model) {
        int result = 0;

        try (PreparedStatement statement = connection.prepareStatement(SQLUser.UPDATE.QUERY)) {
            statement.setString(1, model.getPassword());
            statement.setInt(2, model.getId());
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(User model) {
        int result = -1;

        try (PreparedStatement statement = connection.prepareStatement(SQLUser.DELETE.QUERY)) {
            statement.setInt(1, model.getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getSurname());
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    enum SQLUser {
//        GET("SELECT u.id, u.login, u.password, r.id AS role_id, r.role FROM users AS u LEFT JOIN roles AS r ON u.role = r.id WHERE u.name = (?)"),
        GET("SELECT u.id, u.name, u.surname, u.age, u.login, u.password, u.role_id, r.role AS role FROM users AS u LEFT JOIN roles AS r ON u.role_id = r.id WHERE name = (?)"),
        INSERT("INSERT INTO users (id, name, surname, age, role_id, login, password) VALUES (DEFAULT, (?), (?), (?), (?), (?), (?))"),
        DELETE("DELETE FROM users WHERE id = (?) AND name = (?) AND surname = (?)"),
        UPDATE("UPDATE users SET password = (?) WHERE id = (?)");

        String QUERY;

        SQLUser(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
