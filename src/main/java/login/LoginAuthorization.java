package login;

import user.User;

import static user.UserCreating.EMAIL;
import static user.UserCreating.PASSWORD;

public class LoginAuthorization {
    public static Login getLoginUser(User user) {
        Login login = new Login(EMAIL, PASSWORD);
        login.getEmail();
        login.getPassword();
        return login;
    }

    public static Login getWrongLoginPasswordUser() {
        return new Login("test1256@testtest.ru", "954789");
    }
}
