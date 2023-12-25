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
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static user.UserCreating.*;

public class GetOrderTest {
    User user;
    UserResponse userResponse;
    private OrderResponse orderResponse;
    private String accessToken;

    @Before
    public void setUp() {
        Config.start();
        orderResponse = new OrderResponse();
        user = UserCreating.getUser(email, password, name);
        userResponse = new UserResponse();
    }

    @Test
    @DisplayName("Получение заказа -авторизованный пользователь")
    public void getOrdersWithAuthorizationTest() {
        ValidatableResponse responseUser = userResponse.createUser(user);
        accessToken = responseUser.extract().path("accessToken");
        int status = responseUser.extract().statusCode();
        assertEquals(SC_OK, status);
        ValidatableResponse response = orderResponse.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[4]._id"));
        ingredients.add(response.extract().path("data[6]._id"));
        Order order = new Order(ingredients);
        ValidatableResponse orderResp = orderResponse.createOrder(order);
        int statusCode = orderResp.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        ValidatableResponse getResponse = orderResponse.getOrdersByAuthorization(accessToken);
        int getCode = getResponse.extract().statusCode();
        assertEquals(SC_OK, getCode);
        boolean isGetting = getResponse.extract().path("success");
        assertTrue("Orders are not available", isGetting);
    }

    @Test
    @DisplayName("Получение заказа без авторизации")
    public void getOrdersWithoutAuthTest() {
        ValidatableResponse response = orderResponse.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[4]._id"));
        ingredients.add(response.extract().path("data[6]._id"));
        Order order = new Order(ingredients);
        ValidatableResponse orderResp = orderResponse.createOrder(order);
        int statusCode = orderResp.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        OrderResponse orderResponse = new OrderResponse();
        ValidatableResponse getResponse = orderResponse.getOrdersWithoutAuthorization();
        int getCode = getResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, getCode);
        String message = getResponse.extract().path("message");
        assertEquals("You should be authorised", message);
    }

    @After
    public void delete() {
        if (accessToken != null) {
            userResponse.deleteUser(accessToken);
        }
    }
}
