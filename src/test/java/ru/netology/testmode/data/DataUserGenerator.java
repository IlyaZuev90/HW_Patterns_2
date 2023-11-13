package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataUserGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("ru"));

    private DataUserGenerator() {
    }

    private static void sendRequest(RegistrationUser user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static RegistrationUser getUser(String status) {
        return new RegistrationUser(getRandomLogin(), getRandomPassword(), status);
    }

    public static RegistrationUser getRegisteredUser(String status) {
        RegistrationUser user = getUser(status);
        sendRequest(user);
        return user;
    }

    @Value
    public static class RegistrationUser {
        String login;
        String password;
        String status;
    }
}
