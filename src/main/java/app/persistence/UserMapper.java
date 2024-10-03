package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User login(String name, String password, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "select * from \"user\" where name=? and password=?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    return new User(id, name, password);
                } else {
                    throw new DatabaseException("You have entered an invalid username or password");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void createUser(String name, String password, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "insert into \"user\" (name, password) values (?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, password);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Error: Unable to create account. Please try again later.");
                }

            }

        } catch (SQLException exception) {
            String msg = "Error: Unable to create account";
            if (exception.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Error: Unable to create account. Username already taken.";
            }
            throw new DatabaseException(msg);
        }
    }
}