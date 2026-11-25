package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import data.User;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    @Getter
    private final SelenideElement usernameInput = $("#user-name");
    @Getter
    private final SelenideElement passwordInput = $("#password");
    @Getter
    private final SelenideElement loginButton = $("#login-button");
    @Getter
    private final SelenideElement errorMessage = $("[data-test='error']");
    private final SelenideElement errorCloseButton = $("[data-test='error-button']");

    @Step("Открыть страницу авторизации")
    public LoginPage open() {

        Selenide.open("/");
        return this;

    }

    @Step("Открыть страницу товаров (без авторизации)")
    public LoginPage openInventoryDirectly() {

        Selenide.open("/inventory.html");
        return this;

    }

    @Step("Войти под пользователем {user}")
    public ProductPage login(User user) {

        fillAndSubmit(
                user.getUsername(),
                user.getPassword()
        );
        return new ProductPage();

    }

    @Step("Войти со своими данными. Логин: {username}, пароль: {password}")
    public LoginPage loginWith(String username, String password) {

        fillAndSubmit(username, password);
        return this;

    }

    @Step("Закрыть сообщение об ошибке")
    public LoginPage closeError() {

        errorCloseButton.click();
        return this;

    }

    private void fillAndSubmit(String username, String password) {

        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();

    }

}
