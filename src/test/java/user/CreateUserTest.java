package user;

import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

public class CreateUserTest {
    User user;
    UserResponse userResponse;
    private String accessToken;

    @Before
    public void setUp() {
        Config.start();
        userResponse = new UserResponse();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        user = UserCreating.getUser();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        int statusCode = responseUser.extract().statusCode();
        Assert.assertEquals("User not updated", SC_OK, statusCode);
        boolean isCreate = responseUser.extract().path("success");
        assertTrue("User is not created", isCreate);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserAlreadyExistsTest() {
        user = UserCreating.getUser();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        int statusCode = responseUser.extract().statusCode();
        Assert.assertEquals("User not updated", SC_OK, statusCode);
        boolean isCreate = responseUser.extract().path("success");
        assertTrue("User is not created", isCreate);
        ValidatableResponse responseUserAgain = userResponse.createUser(user);
        int statusCodeForbidden = responseUserAgain.extract().statusCode();
        Assert.assertEquals("User not updated", SC_FORBIDDEN, statusCodeForbidden);
    }

    @Test
    @DisplayName("Создание пользователя без поля Логин")
    public void createUserWithoutLoginTest() {
        user = UserCreating.getUserWithoutLogin();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        int statusCode = responseUser.extract().statusCode();
        Assert.assertEquals("User not updated", SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя без поля Пароль")
    public void createUserWithoutPasswordTest() {
        user = UserCreating.getUserWithoutPassword();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        int statusCode = responseUser.extract().statusCode();
        Assert.assertEquals("User not updated", SC_FORBIDDEN, statusCode);
    }

    @After
    public void delete() {
        if (accessToken != null) {
            userResponse.deleteUser(accessToken);
        }
    }
}
