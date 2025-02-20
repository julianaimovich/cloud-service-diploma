package ru.netology.cloudservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import ru.netology.cloudservice.dto.UserDto;
import ru.netology.cloudservice.util.TestConstants.ServerParams;
import ru.netology.cloudservice.util.builder.UserBuilder;
import ru.netology.cloudservice.utils.Constants;

public class ServerUtils {

    public static void waitForServer() throws InterruptedException {
        String actuatorUrl = ServerParams.SERVER_URI + ServerParams.ACTUATOR_HEALTH_ENDPOINT;
        int retries = 10;
        while (retries > 0) {
            try {
                RestAssured.get(actuatorUrl).then().statusCode(200);
                System.out.println("Application is up and running");
                return;
            } catch (Exception e) {
                System.out.println("Waiting for the server to start. " +
                        "There are still attempts left: " + retries);
                Thread.sleep(5000);
                retries--;
            }
        }
        throw new RuntimeException("Application did not start");
    }

    public static Response login() throws JsonProcessingException {
        UserDto userDto = UserBuilder.getExistentUserForRequest();
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(BaseConverter.convertClassToJsonString(userDto))
                .when()
                .post(Constants.Endpoints.LOGIN)
                .then()
                .extract()
                .response();
    }

    public static void resetCookies() {
        RestAssured.reset();
        CookieFilter cookieFilter = new CookieFilter();
        RestAssured.filters(cookieFilter);
    }
}