package login;

import user.User;

import static user.UserCreating.email;
import static user.UserCreating.password;

public class LoginAuthorization {
    public static Login getLoginUser(User user) {
        Login login = new Login(email, password);
        login.getEmail();
        login.getPassword();
        return login;
    }

    public static Login getWrongLoginPasswordUser() {
        return new Login("test1256@testtest.ru", "954789");
    }
}
