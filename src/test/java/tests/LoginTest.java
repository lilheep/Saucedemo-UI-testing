package tests;

import data.User;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.LoginPage;
import pages.ProductPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@Epic("Sauce")
@Feature("Авторизация")
public class LoginTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();

    @Test
    @DisplayName("Стандартный пользователь успешно логинится")
    @Story("Успешный вход")
    @Tag("smoke")
    void standardUserCanLogin() {

        loginPage.open();
        loginPage.login(User.STANDARD);

        Allure.step("Открылась страница Products", () -> {

            $(".title").shouldHave(exactText("Products"));

        });

    }

    @Test
    @DisplayName("Пользователь с задержкой входа успешно логинится")
    @Story("Успешный вход")
    @Tag("positive")
    void performanceGlitchUserCanLogin() {

        ProductPage productPage = loginPage.open().login(User.PERFORMANCE_GLITCH);

        Allure.step("Открылась страница Products (с увеличенным ожиданием)", () -> {

            productPage.getTitle().shouldHave(exactText("Products"), Duration.ofSeconds(10));

        });

    }

    @Test
    @DisplayName("Проблемный пользователь успешно логинится")
    @Story("Успешный вход")
    void problemUserCanLogin() {

        loginPage.open();
        loginPage.login(User.PROBLEM);

        Allure.step("Открылась страница Products", () -> {

            $(".title").shouldHave(exactText("Products"));

        });

    }

    @Test
    @DisplayName("Заблокированный пользователь не может войти и видит ошибку")
    @Story("Неверные данные")
    void lockedOutUserSeesError() {

        loginPage.open();
        loginPage.login(User.LOCKED_OUT);

        Allure.step("Сообщение о блокировке пользователя", () -> {

            loginPage.getErrorMessage().shouldHave(text("this user has been locked out"));

        });

    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Неверные учетные данные")
    @Story("Неверные данные")
    @CsvSource({
            "'Неверный пароль', standard_user, wrong_password, 'Username and password do not match any user in this service'",
            "'Несуществующий логин', wrong_login, secret_sauce, 'Username and password do not match any user in this service'",
            "'Логин в верхнем регистре', STANDARD_USER, secret_sauce, 'Username and password do not match any user in this service'",
            "'Незаполненный логин', '', secret_sauce, 'Username is required'",
            "'Незаполненный пароль', standard_user, '', 'Password is required'",
            "'Оба поля пустые', '', '', 'Username is required'"
    })
    void invalidCredentialsShowError(String caseName, String username, String password, String errorMessage) {

        loginPage.open();
        loginPage.loginWith(username, password);

        Allure.step("Ошибка: " + errorMessage, () -> {

            loginPage.getErrorMessage().shouldHave(text(errorMessage));

        });

    }

    @Test
    @DisplayName("Сообщение об ошибке можно закрыть")
    @Story("Сообщения об ошибках")
    void errorMessageCanBeClosed() {

        loginPage.open().loginWith("", "");
        loginPage.getErrorMessage().shouldBe(visible);

        loginPage.closeError();

        Allure.step("Сообщение об ошибке закрыто", () -> {

            loginPage.getErrorMessage().shouldNotBe(visible);

        });

    }

    @Test
    @DisplayName("Поле 'Password' скрывает символы")
    @Story("Безопасность")
    void passwordFieldIsMasked() {

        loginPage.open();

        Allure.step("Поле пароль скрыто", () -> {

            loginPage.getPasswordInput().shouldHave(attribute("type", "password"));

        });

    }

    @Test
    @DisplayName("Пользователь может выйти из аккаунта")
    @Story("Выход")
    @Tag("smoke")
    void userCanLogout() {

        ProductPage productPage = loginPage.open().login(User.STANDARD);
        productPage.logout();

        Allure.step("Открыта страница авторизации", () -> {

            loginPage.getLoginButton().shouldBe(visible);

        });

    }

    @Test
    @DisplayName("Просмотр товаров недоступен без авторизации")
    @Story("Безопасность")
    void inventoryIsNotAvailableWithoutLogin() {

        loginPage.openInventoryDirectly();

        Allure.step("Ошибка доступа", () -> {

            loginPage.getErrorMessage().shouldHave(text("when you are logged in"));

        });

    }

}
