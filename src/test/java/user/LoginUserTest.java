package user;

import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import login.Login;
import login.LoginAuthorization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static user.UserCreating.*;

public class LoginUserTest {
    User user;
    UserResponse userResponse;
    private String accessToken;

    @Before
    public void setUp() {
        Config.start();
        user = UserCreating.getUser(email, password, name);
        userResponse = new UserResponse();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginExistUserTest() {
        Login login = LoginAuthorization.getLoginUser(user);
        Boolean isOk = userResponse.loginUser(login).extract().path("success");
        assertTrue(isOk);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithWrongLoginPassword() {
        Login login = LoginAuthorization.getWrongLoginPasswordUser();
        String massage = userResponse.loginUser(login).extract().path("message");
        assertEquals("email or password are incorrect", massage);
    }

    @After
    public void delete() {
        if (accessToken != null) {
            userResponse.deleteUser(accessToken);
        }
    }

}
