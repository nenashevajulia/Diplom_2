package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import login.Login;

import static io.restassured.RestAssured.given;

public class UserResponse {
    private final String USER = "/api/auth/user ";
    private final String REGISTER = "/api/auth/register";
    private final String LOGIN = "/api/auth/login ";



    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given().contentType(ContentType.JSON).and().body(user).when().post(REGISTER).then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Login login) {
        return given().contentType(ContentType.JSON).and().body(login).when().post(LOGIN).then();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse updateUserWithAuthorization(User user, String accessToken) {
        return given().contentType(ContentType.JSON).header("Authorization", accessToken).and().body(user).patch(USER).then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuthorization(User user) {
        return given().contentType(ContentType.JSON).and().body(user).patch(USER).then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().delete(USER).then();
    }

}
