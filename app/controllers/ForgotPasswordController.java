package controllers;

import play.mvc.*;


public class ForgotPasswordController extends Controller {
    public Result forgotpassword() {
        return ok(views.html.forgotpassword.render());
    }
}
