package external.letiuka.modelviewcontroller.view.content;

import java.util.ListResourceBundle;

public class EnContent extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"login.title", "Log In Page"},
            {"login.button", "Log in"},
            {"login.password", "Password"},
            {"login.login-label", "Login"},

            {"navbar.login", "Log in"}
    };
}
