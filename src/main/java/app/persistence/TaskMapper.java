package app.persistence;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskMapper {


    public static List<Task> getAllTasksPerUser(int user_id, ConnectionPool connectionPool) throws DatabaseException {
        List<Task> taskList = new ArrayList<Task>();

        String sql = "select * from task where user_id=?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, user_id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    boolean done = rs.getBoolean("done");

                    taskList.add(new Task(id, name, done, user_id));
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Error");
        }
        return taskList;
    }

    public static Task addTask(User user, String taskName, ConnectionPool connectionPool) throws DatabaseException {

        Task newTask = null;

        String sql = "insert into task (name, done, user_id) values (?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, taskName);
                ps.setBoolean(2, false);
                ps.setInt(3, user.getId());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {

                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int newId = rs.getInt(1);
                    newTask = new Task(newId, taskName, false, user.getId());

                } else {
                    throw new DatabaseException("Failed to add task " + taskName);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database connection failed");
        }
        return newTask;
    }

    public static void setDoneTo(boolean done, int taskId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "update task set done = ? where id = ?";


        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setBoolean(1, done);
                ps.setInt(2, taskId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {

                    throw new DatabaseException("Failed to update task list");

                }

            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update task list");

        }
    }

    public static void delete(int taskId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "delete from task where id = ?";


        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, taskId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {

                    throw new DatabaseException("Failed to delete task");

                }

            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete task");

        }
    }

    public static Task getTaskById(int taskId, ConnectionPool connectionPool) throws DatabaseException {

        Task task = null;

        String sql = "select * from task where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, taskId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    boolean done = rs.getBoolean("done");
                    int user_id = rs.getInt("user_id");
                    task = new Task(id, name, done, user_id);

                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to get task by id " + taskId);
        }
        return task;
    }

    public static void update(int taskId, String taskName, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "update task set name = ? where id = ?";


        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, taskName);
                ps.setInt(2, taskId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {

                    throw new DatabaseException("Failed to update task list");

                }

            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update task list");

        }

    }
}
