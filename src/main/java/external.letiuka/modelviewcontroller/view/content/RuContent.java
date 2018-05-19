package external.letiuka.modelviewcontroller.view.content;

import java.util.ListResourceBundle;

public class RuContent extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { "login.title", "Страница входа" },
            { "login.button", "Войти" },
            { "login.password", "Пароль" },
            { "login.login-label", "Логин" },

            {"navbar.login","Войти"}
    };
}
