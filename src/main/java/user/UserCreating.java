package user;

import io.qameta.allure.Step;

public class UserCreating {
    public static final String EMAIL = "user156@test.ru";
    public static final String PASSWORD = "pass12345";
    public static final String NAME = "Пользователь";

    @Step("Создание пользователя со всеми обязательными полями")
    public static User getUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setName(NAME);
        return user;
    }
    @Step("Создание пользователя без логина")
    public static User getUserWithoutLogin() {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setName(NAME);
        return user;
    }
    @Step("Создание пользователя без пароля")
    public static User getUserWithoutPassword() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setName(NAME);
        return user;
    }
}
