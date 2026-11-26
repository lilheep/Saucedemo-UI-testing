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
import pages.LoginPage;
import pages.ProductPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;

@Epic("Sauce")
@Feature("Каталог товаров")
public class ProductCardTest extends BaseTest {

    private ProductPage productPage;

    @BeforeEach
    void login() {

        productPage = new LoginPage().open().login(User.STANDARD);

    }

    @Test
    @DisplayName("При нажатии на кнопку добавления товара, кнопка меняется на 'Remove', счетчик корзины прибавляется +1")
    @Story("Добавление товара в корзину")
    @Tag("smoke")
    void addOneProductInCart() {

        productPage.addToCart(0);

        Allure.step("Кнопка стала Remove, счетчик корзины изменился", () -> {

            productPage.getButtonAt(0).shouldHave(exactText("Remove"));
            productPage.getCartBadge().shouldHave(exactText("1"));

        });

    }

    @Test
    @DisplayName("Добавление нескольких товаров в корзину")
    @Story("Добавление товара в корзину")
    @Tag("regression")
    void addSeveralProducts() {

        productPage.addToCart(0)
                    .addToCart(1)
                    .addToCart(2);

        Allure.step("Счетчик количества товаров в корзине соответсвует количеству товаров в ней", () -> {

            productPage.getCartBadge().shouldHave(exactText("3"));

        });

    }

    @Test
    @DisplayName("Товар удаляется, кнопка становится 'Add to cart', счетчик исчезает")
    @Story("Удаление товара из корзины")
    @Tag("regression")
    void removeProduct() {

        productPage.addToCart(0)
                    .removeFromCart(0);

        Allure.step("Кнопка вернулась к Add to cart, счетчик исчез", () -> {

            productPage.getButtonAt(0).shouldHave(exactText("Add to cart"));
            productPage.getCartBadge().shouldNot(exist);

        });

    }

    @Test
    @DisplayName("Удаление одного из двух товаров в корзине")
    @Story("Удаление товара из корзины")
    @Tag("regression")
    void removeOneOfTwoProduct() {

        productPage.addToCart(0)
                    .addToCart(1)
                    .removeFromCart(0);

        Allure.step("В корзине остался один товар, у первого товара кнопка вернулась к Add to cart", () -> {

            productPage.getButtonAt(0).shouldHave(exactText("Add to cart"));
            productPage.getButtonAt(1).shouldHave(exactText("Remove"));
            productPage.getCartBadge().shouldHave(exactText("1"));

        });

    }




}
