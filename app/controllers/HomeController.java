package controllers;

import forms.LoginForm;
import forms.RegisterForm;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;

import javax.inject.Inject;
import java.util.Optional;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private final Form<User> userForm;
    private final Form<LoginForm> loginForm;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;
    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.loginForm = formFactory.form(LoginForm.class);

        this.userForm = formFactory.form(User.class);
    }

    public Result index(Http.Request request) {
        return ok(views.html.login.render(loginForm, request, messagesApi.preferred(request)));
    }

    public Result login(Http.Request request) {
        Form<LoginForm> form = formFactory.form(LoginForm.class).bindFromRequest(request);
        if (form.hasErrors()) {
            return badRequest(views.html.login.render(form, request, messagesApi.preferred(request)));
        } else {
            LoginForm data = form.get();
            Optional<User> userOpt = User.find.query().where().eq("email", data.email).findOneOrEmpty();
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.checkPassword(data.password)) {
                    // Password matches, create session
                    return redirect(routes.DashboardController.dashboard()).addingToSession(request, "user", user.id.toString());
                } else {
                    return unauthorized("Invalid email or password");
                }
            } else {
                return unauthorized("Invalid email or password");
            }
        }
    }

    public Result register(Http.Request request) {
        Form<RegisterForm> form = formFactory.form(RegisterForm.class).bindFromRequest(request);
        if (form.hasErrors()) {
            return badRequest(views.html.register.render(userForm, request, messagesApi.preferred(request)));
        } else {
            RegisterForm data = form.get();
            if (!data.password.equals(data.confirmPassword)) {
                return badRequest("Passwords do not match");
            }
            User user = new User(data.username, data.email, data.phoneNumber, data.password);
            user.save();
            return redirect(routes.HomeController.index());
        }
    }

    public Result dashboard(Http.Request request) {
        // Assuming you have a session value "user" to identify the logged-in user
        String userId = request.session().getOptional("user").orElse(null);
        if (userId == null) {
            return redirect(routes.HomeController.index());
        }

        // Render the dashboard view
        return ok(views.html.dashboard.render(userId, request, messagesApi.preferred(request)));
    }

    public Result logout(Http.Request request) {
        return redirect(routes.DashboardController.dashboard()).removingFromSession(request, "user");
    }

}

