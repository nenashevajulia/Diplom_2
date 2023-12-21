package user;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserCreating {
    public static String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    public static String password = RandomStringUtils.randomNumeric(6);
    public static String name = RandomStringUtils.randomAlphabetic(10);

    public static User getUser(String email, String password, String name) {
        User user = new User(email, password, name);
        return user;
    }

    @Step("Создание пользователя без логина")
    public static User getUserWithoutLogin() {
        User user = new User();
        user.setPassword(password);
        user.setName(name);
        return user;
    }

    @Step("Создание пользователя без пароля")
    public static User getUserWithoutPassword() {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
