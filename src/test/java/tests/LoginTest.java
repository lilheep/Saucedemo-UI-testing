package tests;

import data.User;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

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
}
