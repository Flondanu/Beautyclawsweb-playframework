package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RegisterController extends Controller {

    private final Form<User> userForm;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    public RegisterController(HttpExecutionContext ec, FormFactory formFactory, MessagesApi messagesApi) {
        this.ec = ec;
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;

        this.userForm = formFactory.form(User.class);
    }

    public Result register(Http.Request request) {return ok(views.html.register.render(userForm, request, messagesApi.preferred(request)));}


    public Result handleRegister(Http.Request request) {
        Form<User> boundForm = userForm.bindFromRequest(request);
        String confirmPassword = formFactory.form().bindFromRequest(request).get("confirm_password");

        if (boundForm.hasErrors()) {
            logger.error("registration errors = {}", boundForm.errors());
            return badRequest(views.html.register.render(boundForm, request, messagesApi.preferred(request)));
        }

        User user = boundForm.get();

//        // Validate email format
//        if (!user.isValidEmailFormat(user.getEmail())) {
//            boundForm = boundForm.withError(new ValidationError("email", "Invalid email format"));
//            logger.error("registration errors = {}", boundForm.errors());
//            return badRequest(views.html.register.render(boundForm, request, messagesApi.preferred(request)));
//        }
        logger.info("Password:::: "+user.getPassword());
        logger.info("Confirm Password:::: "+confirmPassword);
        if (!user.getPassword().equals(confirmPassword)) {
            List<String> errs = new ArrayList<>();
            errs.add("Passwords do not match");
            Form<User> boundForm1 = boundForm.withError(new ValidationError("password", errs, new ArrayList<>()));
            logger.error("registration errors = {}", boundForm1.errors());
            return badRequest(views.html.register.render(boundForm1, request, messagesApi.preferred(request)));
        }

        // Check for existing email
        User existingUser = User.find.query().where().eq("email", user.getEmail()).findOne();
        if (existingUser != null) {
            boundForm = boundForm.withError(new ValidationError("email", "Email is already taken"));
            logger.error("registration errors = {}", boundForm.errors());
            return badRequest(views.html.register.render(boundForm, request, messagesApi.preferred(request)));
        }

//        user.setPassword(user.getPassword());
        user.hashPassword();
        logger.info("Password:::: "+user.getPassword());
        logger.info("saving user");
        user.save();


        return redirect(routes.HomeController.index()).flashing("success", "User successfully registered");
    }
}
