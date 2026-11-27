package tests;

import data.User;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Sauce")
@Feature("Оформление заказа")
public class CheckoutTest extends BaseTest {

    private ProductPage productPage;

    @BeforeEach
    void login() {

        productPage = new LoginPage().open().login(User.STANDARD);

    }

    private CheckoutInfoPage openCheckoutForm() {

        return productPage.addToCart(0)
                .openCart()
                .checkout();

    }

    private CheckoutCompletePage completeOrder() {

        return openCheckoutForm()
                .fillInfo("Ivan", "Ivanov", "12345")
                .finish();

    }

    private String readHeader(File file) throws IOException {

        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return new String(inputStream.readNBytes(4), StandardCharsets.US_ASCII);
        }

    }

    @Test
    @DisplayName("Пользователь оформляет заказ от корзины до подтверждения")
    @Tag("smoke")
    void userCanCompleteOrder() {

        String name = productPage.getProductNameAt(0);

        CheckoutOverviewPage overviewPage = openCheckoutForm().fillInfo(
                "Ivanov", "Ivan", "12345"
        );

        Allure.step("На странице обзора заказа выбранный товар", () -> {

            overviewPage.getItems().shouldHave(exactTexts(name));

        });

        CheckoutCompletePage completePage = overviewPage.finish();

        Allure.step("Заказ оформлен: отображается подтверждение заказа", () -> {

            webdriver().shouldHave(urlContaining("/checkout-complete.html"));
            completePage.getHeader().shouldHave(exactText("Thank you for your order!"));

        });

    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Форма данных для заказа: обязательные поля")
    @Story("Валидация данных")
    @CsvSource({
            "'Пустое имя', '', Ivanov, 12345, 'First Name is required'",
            "'Пустая фамилия', Ivan, '', 12345, 'Last Name is required'",
            "'Пустой индекс', Ivan, Ivanov, '', 'Postal Code is required'",
            "'Все поля пустые', '', '', '', 'First Name is required'"
    })
    void formRequiresAllFields(String caseName,
                               String firstName,
                               String lastName,
                               String postalCode,
                               String errorMessage) {

        CheckoutInfoPage checkoutInfoPage = openCheckoutForm();
        checkoutInfoPage.fillInfo(firstName, lastName, postalCode);

        Allure.step("Показана ошибка: " + errorMessage, () -> {

            checkoutInfoPage.getErrorMessage().shouldHave(text(errorMessage));

        });

    }

    @Test
    @DisplayName("Кнопка Back Home после заказа ведёт в каталог, корзина пустая")
    @Story("Навигация")
    @Tag("regression")
    void backHomeAfterOrderOpensCatalogWithEmptyCart() {

        ProductPage catalog = completeOrder().backHome();

        Allure.step("Открыт каталог, корзина пустая", () -> {

            webdriver().shouldHave(urlContaining("/inventory.html"));
            catalog.getTitle().shouldHave(exactText("Products"));
            catalog.getCartBadge().shouldNot(exist);

        });

    }

    @Test
    @DisplayName("PDF-счет заказа скачивается")
    @Story("После заказа")
    @Tag("regression")
    void orderReceiptCanBeDownloaded() throws IOException {

        File receipt = completeOrder().downloadReceipt();
        String header = readHeader(receipt);

        Allure.step("Файл скачан - имя по шаблону, не пустой, расширение PDF", () -> {

            assertTrue(receipt.getName().matches(
                    "swag-labs-order-\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}\\.pdf"
            ));

            assertTrue(receipt.length() > 0, "Файл пустой");
            assertEquals("%PDF", header, "Файл начинается не с PDF-заголовка");

        });

    }

}
