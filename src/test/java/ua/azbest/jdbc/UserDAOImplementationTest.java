package ua.azbest.jdbc;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.azbest.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserDAOImplementationTest {

    @NotNull
    private DAO<User, String> dao;

    @NotNull
    private Connection connection;

    @Before
    public void before() {
        try {
            String user = "root";
            String password = "!NPUpassword2019";
            String url = "jdbc:mysql://localhost:3306/npu"+
                    "?verifyServerCertificate=false"+
                    "&useSSL=false"+
                    "&requireSSL=false"+
                    "&useLegacyDatetimeCode=false"+
                    "&amp"+
                    "&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, user, password);
            dao = new UserDAOImplementation(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenUserIsNotExistThenReturnEmptyUserObj() {
        final User user = dao.read("xxx");
        assertThat(user.getId(), is(-1));
    }

    @Test
    public void whenGetUserWhichExistThenReturnUser() {
        final User user = dao.read("Petro");
        final User expected = new User();
        expected.setId(2);
        expected.setName("Petro");
        expected.setSurname("Shokolad");
        expected.setAge(45);
        expected.setRole(new User.Role(2, "user"));
        expected.setLogin("roshen");
        expected.setPassword("candy");

        assertThat(user, is(expected));
    }

    @Test
    public void whenAddUserWhichNotExistThenReturnUser() {
        final User user = new User(105, "Auto", "Created", 25, new User.Role(2, "user"), "generated", "1111");
        final int result = dao.create(user);
        assertThat(result, is(1));
        //Clear test data.
        //dao.delete(dao.read("Auto"));
    }

    @Test
    public void deleteWhenUserNotExist() {
        assertEquals(dao.delete(new User(105, "Test", "Test", 105, new User.Role(1, "admin"), "test", "test")), 0);
    }

    @Test
    public void deleteWhenUserIsExist() {
        final User user = new User(0, "Test", "Test", 105, new User.Role(1, "admin"), "test", "test");
        dao.create(user);
        final User state = dao.read("test");
        boolean before = state.getId() != -1;
        user.setId(state.getId());

        final int after = dao.delete(user);
        assertTrue(before);
        assertEquals(after, 1);
    }

    @Test
    public void whenUpdateExistUserThenPasswordUpdated() {
        final User user = new User(0, "Test", "Test", 105, new User.Role(1, "admin"), "test", "test");
        dao.create(user);
        final User gutted = dao.read("test");
        gutted.setPassword("updated");
        final int result = dao.update(gutted);
        final User updated = dao.read("test");
        assertThat(result, is(1));
        assertThat(updated.getPassword(), is("updated"));

        //Clear test data.
        dao.delete(updated);
    }

    @Test
    public void whenUpdateNotExistedUserThenPasswordUpdated() {
        assertThat(dao.update(new User()), is(0));
    }

}