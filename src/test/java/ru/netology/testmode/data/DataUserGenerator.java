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
    private static final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataUserGenerator() {
    }

    private static void sendRequest(RegistrationUser user) {
        given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static RegistrationUser getUser(String status) {
        RegistrationUser user = new RegistrationUser(getRandomLogin(), getRandomPassword(), status);
        return user;
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
