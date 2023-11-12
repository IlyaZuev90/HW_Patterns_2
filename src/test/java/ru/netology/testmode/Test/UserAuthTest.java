package ru.netology.testmode.Test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.Data.DataUserGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.Data.DataUserGenerator.getRegisteredUser;
import static ru.netology.testmode.Data.DataUserGenerator.getUser;
import static ru.netology.testmode.Data.DataUserGenerator.getRandomLogin;
import static ru.netology.testmode.Data.DataUserGenerator.getRandomPassword;

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
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldReturnErrorIfUserNotRegistered() {
        DataUserGenerator.RegistrationUser notRegisteredUser = getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((Condition.visible));
    }

    @Test
    public void shouldReturnErrorIfUserBlocked() {
        DataUserGenerator.RegistrationUser blockedUser = getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe((Condition.visible));
    }

    @Test
    public void shouldReturnErrorIfLoginInvalid() {
        DataUserGenerator.RegistrationUser registeredUser = getRegisteredUser("active");
        String invalidLogin = getRandomLogin();

        $("[data-test-id='login'] input").setValue(invalidLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((Condition.visible));
    }

    @Test
    public void shouldReturnErrorIfPasswordInvalid() {
        DataUserGenerator.RegistrationUser registeredUser = getRegisteredUser("active");
        String invalidPassword = getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(invalidPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe((Condition.visible));
    }
}