package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataUserGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataUserGenerator.getRegisteredUser;
import static ru.netology.testmode.data.DataUserGenerator.getUser;
import static ru.netology.testmode.data.DataUserGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataUserGenerator.getRandomPassword;

public class UserAuthTest {

    @BeforeEach
    public void setupAll() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldLoginWithRegisteredUserSuccessfully() {
        DataUserGenerator.RegistrationUser registeredUser = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Личный кабинет"));

    }

    @Test
    public void shouldReturnErrorIfUserNotRegistered() {
        DataUserGenerator.RegistrationUser notRegisteredUser = getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldReturnErrorIfUserBlocked() {
        DataUserGenerator.RegistrationUser blockedUser = getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldReturnErrorIfLoginInvalid() {
        DataUserGenerator.RegistrationUser registeredUser = getRegisteredUser("active");
        String invalidLogin = getRandomLogin();

        $("[data-test-id='login'] input").setValue(invalidLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldReturnErrorIfPasswordInvalid() {
        DataUserGenerator.RegistrationUser registeredUser = getRegisteredUser("active");
        String invalidPassword = getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(invalidPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}