package order;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderResponse extends Config {
    private static final String GET_INGREDIENTS = "/api/ingredients";
    private static final String CREATE_ORDER = "/api/orders";

    @Step("Получение списка ингридиентов")
    public ValidatableResponse getIngredients() {
        return given().contentType(ContentType.JSON).get(GET_INGREDIENTS).then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given().contentType(ContentType.JSON).and().body(order).when().post(CREATE_ORDER).then();
    }

    @Step("Создание заказа + авторизация")
    public ValidatableResponse createOrderByAuthorization(String accessToken, Order order) {
        return given().contentType(ContentType.JSON).header("Authorization", accessToken).and().body(order).when().post(CREATE_ORDER).then();
    }


    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(CREATE_ORDER)
                .then();
    }

    @Step("Получение заказа + авторизация")
    public ValidatableResponse getOrdersByAuthorization(String accessToken) {
        return given().contentType(ContentType.JSON).header("Authorization", accessToken).get(CREATE_ORDER).then();
    }

    @Step("Получение заказа")
    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given().contentType(ContentType.JSON).get(CREATE_ORDER).then();
    }
}
