package user;

import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import login.Login;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static user.UserCreating.*;

public class ChangeUserDataTest {
    User user;
    UserResponse userResponse;
    private String accessToken;

    @Before
    public void setUp() {
        Config.start();
        user = UserCreating.getUser(email, password, name);
        userResponse = new UserResponse();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeUserData() {
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        Login loginUser = new Login(user.getEmail(), user.getPassword());
        userResponse.loginUser(loginUser);
        String userUpdateName = user.getName() + "newName";
        user.setName(userUpdateName);
        user.setAccessToken(responseUser.extract().path("accessToken"));
        ValidatableResponse responseUpdate = userResponse.updateUserWithAuthorization(user, accessToken);
        int statusCode = responseUpdate.extract().statusCode();
        Assert.assertEquals("User not updated", SC_OK, statusCode);
        String newName = responseUpdate.extract().path("user.name");
        Assert.assertEquals("Name failed", userUpdateName, newName);

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuth() {
        String userUpdateName = user.getName() + "newName";
        user.setName(userUpdateName);
        ValidatableResponse responseUpdate = userResponse.updateUserWithoutAuthorization(user);
        int statusCode = responseUpdate.extract().statusCode();
        Assert.assertEquals("You should be authorised", SC_UNAUTHORIZED, statusCode);
        String actualName = responseUpdate.extract().path("user.name");
        Assert.assertNotEquals("E-mail в ответе сервера не совпадает актуальным", userUpdateName, actualName);
    }

    @After
    public void delete() {
        if (accessToken != null) {
            userResponse.deleteUser(accessToken);
        }
    }
}
