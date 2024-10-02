package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool) {

        String name = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {

            User user = UserMapper.login(name, password, connectionPool);
            ctx.sessionAttribute("currentsUser", user);
            ctx.render("welcome.html");

        } catch (DatabaseException exception) {

            ctx.attribute("message", exception.getMessage());
            ctx.render("index.html");
        }

    }
}
