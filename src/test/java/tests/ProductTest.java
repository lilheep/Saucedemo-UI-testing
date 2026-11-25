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
import pages.ProductCard;
import pages.ProductPage;

import java.util.List;

import static com.codeborne.selenide.Condition.*;

@Epic("Sauce")
@Feature("Каталог товаров")
public class ProductTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();

    @Test
    @DisplayName("Стандартный пользователь: отображение карточек товаров")
    @Story("Карточка товара")
    @Tag("regression")
    void standardUserSeesCompleteProductCards() {

        ProductPage productPage = loginPage.open().login(User.STANDARD);

        List<ProductCard> productCards = productPage.getProductCards();

        for (ProductCard card : productCards) {

            Allure.step("Проверка карточки: " + card.title().text(), () -> {

                card.image().shouldBe(image);
                card.title().shouldHave(matchText("\\S"));
                card.description().shouldHave(matchText("\\S"));
                card.price().shouldHave(matchText("^\\$\\d+\\.\\d{2}$"));
                card.addToCartButton().shouldHave(exactText("Add to cart"));

            });
        }

    }

    /* Под передлку

    @Test
    @DisplayName("Стандартный пользователь: у товаров разные картинки")
    @Story("Картинки товаров")
    @Tag("regression")
    void standardUserSeesUniqueImages() {

        ProductPage productPage = loginPage.open().login(User.STANDARD);

        assertAllImagesUnique(productPage.getProductImagesSource());

    }

    @Test
    @DisplayName("problem_user: у товаров разные картинки")
    @Story("Картинки товаров")
    @Tag("regression")
    @Tag("known-bug")
    void problemUserSeesUniqueImages() {

        ProductPage productPage = loginPage.open().login(User.PROBLEM);

        assertAllImagesUnique(productPage.getProductImagesSource());

    }

    private void assertAllImagesUnique(List<String> sources) {

        Allure.step("У всех товаров разные картинки", () -> {

            Set<String> unique = new HashSet<>(sources);
            assertEquals(sources.size(), unique.size(),
                    "У товаров повторяются картинки: " + sources);

        });

    }

     */

}
