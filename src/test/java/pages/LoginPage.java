package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import data.User;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("#user-name");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login-button");

    @Step("Открыть страницу авторизации")
    public LoginPage open() {

        Selenide.open("/");
        return this;

    }

    @Step("Войти под пользователем {user}")
    public void login(User user) {

        usernameInput.setValue(user.getUsername());
        passwordInput.setValue(user.getPassword());
        loginButton.click();

    }

}
