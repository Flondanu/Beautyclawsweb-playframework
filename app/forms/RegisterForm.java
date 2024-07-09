package forms;

import play.data.validation.Constraints;

public class RegisterForm {
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String phoneNumber;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String confirmPassword;

    public @Constraints.Required String getUsername() {
        return username;
    }

    public void setUsername(@Constraints.Required String username) {
        this.username = username;
    }

    public @Constraints.Required String getEmail() {
        return email;
    }

    public void setEmail(@Constraints.Required String email) {
        this.email = email;
    }

    public @Constraints.Required String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Constraints.Required String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Constraints.Required String getPassword() {
        return password;
    }

    public void setPassword(@Constraints.Required String password) {
        this.password = password;
    }

    public @Constraints.Required String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@Constraints.Required String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
