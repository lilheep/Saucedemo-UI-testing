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
import pages.CartPage;
import pages.LoginPage;
import pages.ProductPage;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.CollectionCondition.size;

@Epic("Sauce")
@Feature("Корзина")
public class CartTest extends BaseTest {

    private ProductPage productPage;

    @BeforeEach
    void login() {

        productPage = new LoginPage().open().login(User.STANDARD);

    }

    @Test
    @DisplayName("Добавленный товар попадает в корзину с тем же именем, ценой и описанием")
    @Story("Содержимое корзины")
    @Tag("smoke")
    void addedProductAppearsInCart() {

        String name = productPage.getProductNameAt(0);
        String price = productPage.getProductPriceAt(0);
        String desc = productPage.getProductDescAt(0);

        CartPage cartPage = productPage.addToCart(0).openCart();

        Allure.step("В корзине тот же товар в количестве 1", () -> {

            webdriver().shouldHave(urlContaining("/cart.html"));
            cartPage.getItems().shouldHave(size(1));
            cartPage.getNameItemAt(0).shouldHave(exactText(name));
            cartPage.getPriceItemAt(0).shouldHave(exactText(price));
            cartPage.getDescriptionItemAt(0).shouldHave(exactText(desc));
            cartPage.getQuantityItemAt(0).shouldHave(exactText("1"));

        });

    }

    @Test
    @DisplayName("В корзину можно добавить несколько товаров")
    @Story("Содержимое корзины")
    @Tag("smoke")
    void addSeveralProductInCart() {

        CartPage cartPage = productPage.addToCart(0)
                                        .addToCart(1)
                                        .openCart();

        Allure.step("В корзину добавлены 2 товара", () -> {

            cartPage.getItems().shouldHave(size(2));

        });

    }

    @Test
    @DisplayName("Товары попадают в корзину в том же порядке")
    @Story("Содержимое корзины")
    @Tag("regression")
    void severalProductsAppearInCart() {

        List<String> products = List.of(

                productPage.getProductNameAt(0),
                productPage.getProductNameAt(2),
                productPage.getProductNameAt(1)

        );

        CartPage cartPage = productPage.addToCart(0)
                .addToCart(2)
                .addToCart(1)
                .openCart();

        Allure.step("Названия товаров в корзине совпадают с добавленными (в том же порядке)", () -> {

            cartPage.getNames().shouldHave(exactTexts(products));

        });

    }

    @Test
    @DisplayName("Можно зайти в корзину, если в ней нету товаров")
    @Story("Содержимое корзины")
    @Tag("regression")
    void emptyCartHasNoItems() {

        CartPage cartPage = productPage.openCart();

        Allure.step("В корзине нет товаров", () -> {

            cartPage.getTitle().shouldHave(exactText("Your Cart"));
            cartPage.getItems().shouldHave(size(0));

        });

    }

    @Test
    @DisplayName("Товар можно удалить из корзины на странице корзины")
    @Story("Удаление из корзины")
    @Tag("regression")
    void productCanBeRemovedInCart() {

        String remainingName = productPage.getProductNameAt(1);

        CartPage cartPage = productPage.addToCart(0)
                                        .addToCart(1)
                                        .openCart();

        cartPage.removeFromCart(0);

        Allure.step("В корзине остался только второй товар", () -> {

            cartPage.getItems().shouldHave(size(1));
            cartPage.getNameItemAt(0).shouldHave(exactText(remainingName));

        });

    }

    @Test
    @DisplayName("Кнопка Continue Shopping возвращает к каталогу продуктов")
    @Story("Навигация")
    @Tag("regression")
    void continueShopping() {

        CartPage cartPage = productPage.openCart();
        ProductPage backToProducts = cartPage.continueShopping();

        Allure.step("Открылась страница с товарами", () -> {

            webdriver().shouldHave(urlContaining("/inventory.html"));
            backToProducts.getTitle().shouldHave(exactText("Products"));

        });

    }

    @Test
    @DisplayName("Кнопка Continue Shopping возвращает на страницу товаров, корзина сохраняется")
    @Story("Навигация")
    @Tag("regression")
    void continueShoppingKeepsCart() {

        CartPage cartPage = productPage
                .addToCart(0)
                .openCart();

        ProductPage backToProducts = cartPage.continueShopping();

        Allure.step("Открылась страница с товарами, в корзине сохранился один товар", () -> {

            webdriver().shouldHave(urlContaining("/inventory.html"));
            backToProducts.getTitle().shouldHave(exactText("Products"));
            backToProducts.getCartBadge().shouldHave(exactText("1"));

        });
    }

}
