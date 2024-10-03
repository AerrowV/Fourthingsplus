package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskController {
    public static void addTask(Context ctx, ConnectionPool connectionPool) {

        User user = ctx.sessionAttribute("currentUser");
        String taskName = ctx.formParam("addtask");
        try {
            Task newTask = TaskMapper.addTask(user, taskName, connectionPool);
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void done(Context ctx, boolean done, ConnectionPool connectionPool) {

        int task_id = Integer.parseInt(ctx.formParam("task_id"));

        try {

            User user = ctx.sessionAttribute("currentUser");
            TaskMapper.setDoneTo(done, task_id, connectionPool);
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("tasks.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void delete(Context ctx, ConnectionPool connectionPool) {

        int task_id = Integer.parseInt(ctx.formParam("task_id"));

        try {

            User user = ctx.sessionAttribute("currentUser");
            TaskMapper.delete(task_id, connectionPool);
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("tasks.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void edit(Context ctx, ConnectionPool connectionPool) {

        int task_id = Integer.parseInt(ctx.formParam("task_id"));

        try {
            Task task = TaskMapper.getTaskById(task_id, connectionPool);
            ctx.attribute("task", task);
            ctx.render("edittask.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }

    public static void update(Context ctx, ConnectionPool connectionPool) {

        int task_id = Integer.parseInt(ctx.formParam("task_id"));
        String taskName = ctx.formParam("task_name");
        try {
            TaskMapper.update(task_id, taskName, connectionPool);
            User user = ctx.sessionAttribute("currentUser");
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("tasks.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }



    }
}
