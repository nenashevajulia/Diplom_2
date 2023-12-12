package config;

import io.restassured.RestAssured;

public class Config {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    public static String getBaseUri() {
        return BASE_URL;
    }

    public static void start() {
        RestAssured.baseURI = Config.getBaseUri();
    }
}
