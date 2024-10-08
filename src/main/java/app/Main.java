package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.TaskController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fourthingsplus";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing

        app.get("/", ctx -> ctx.render("index.html"));
        app.post("/login", ctx -> UserController.login(ctx, connectionPool));
        app.get("/createuser", ctx -> ctx.render("createuser.html"));
        app.post("/createuser", ctx -> UserController.createUser(ctx, connectionPool));
        app.get("/logout", ctx -> UserController.logout(ctx));
        app.post("/addtask", ctx -> TaskController.addTask(ctx, connectionPool));
        app.post("/done", ctx -> TaskController.done(ctx, true, connectionPool));
        app.post("/undo", ctx -> TaskController.done(ctx, false, connectionPool));
        app.post("/delete", ctx -> TaskController.delete(ctx, connectionPool));
        app.post("/edittask", ctx -> TaskController.edit(ctx, connectionPool));
        app.post("/updatetask", ctx -> TaskController.update(ctx, connectionPool));
    }
}