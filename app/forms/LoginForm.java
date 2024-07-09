package forms;

import play.data.validation.Constraints;

public class LoginForm {
    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;
}