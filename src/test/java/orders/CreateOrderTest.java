package orders;

import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserCreating;
import user.UserResponse;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    private Order order;
    private OrderResponse orderResponse;
    private String accessToken;
    User user;
    UserResponse userResponse;

    @Before
    public void setUp() {
        Config.start();
        orderResponse = new OrderResponse();
        user = UserCreating.getUser();
        userResponse = new UserResponse();
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
    }

    @DisplayName("Создание заказа без авторизации")
    @Test
    public void createOrderWithoutAuthorizationTest() {
        ValidatableResponse response = orderResponse.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[0]._id"));
        ingredients.add(response.extract().path("data[3]._id"));
        Order order = new Order(ingredients);
        ValidatableResponse responseCreateOrder = orderResponse.createOrder(order);
        int statusCode = responseCreateOrder.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        boolean isCreate = response.extract().path("success");
        assertTrue("You should be authorised", isCreate);
    }

    @DisplayName("Создание заказа с авторизацией и ингридиентами")
    @Test
    public void createOrderTest() {
        ValidatableResponse response = orderResponse.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[2]._id"));
        ingredients.add(response.extract().path("data[4]._id"));
        order = new Order(ingredients);

        ValidatableResponse responseOrder = orderResponse.createOrderByAuthorization(accessToken, order);
        int statusCode = responseOrder.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        String order = responseOrder.extract().body().asString();
        assertThat(order, containsString("status"));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    public void createOrderWithoutIngredientsTest() {
        List<String> ingredients = new ArrayList<>();
        order = new Order(ingredients);
        ValidatableResponse response = orderResponse.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);
        String message = response.extract().path("message");
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов.")
    public void createOrderWithInvalidIngTest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("c7a791b5cc6c");
        ingredients.add("0df21886af");
        order = new Order(ingredients);
        ValidatableResponse response = orderResponse.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @After
    public void delete() {
        if (accessToken != null) {
            userResponse.deleteUser(accessToken);
        }
    }
}
