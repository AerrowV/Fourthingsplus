package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.persistence.TaskMapper;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool) {

        String name = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {

            User user = UserMapper.login(name, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // Get tasks belonging to this user in DB
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("tasks.html");

        } catch (DatabaseException exception) {

            ctx.attribute("message", exception.getMessage());
            ctx.render("index.html");
        }

    }

    public static void createUser(Context ctx, ConnectionPool connectionPool) {

        String name = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        // Validering af passwords

        if (password1.equals(password2)) {

            try {

                UserMapper.createUser(name, password1, connectionPool);
                ctx.attribute("message", "Account created successfully");
                ctx.render("index.html");

            } catch (DatabaseException e) {

                ctx.attribute("message", e.getMessage());
                ctx.render("createuser.html");
            }
        } else {
                ctx.attribute("message", "Passwords do not match");
                ctx.render("createuser.html");
            }
        }

    public static void logout(Context ctx) {

        // Invalidate session
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
