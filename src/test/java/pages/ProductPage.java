package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductPage {

    @Getter
    private final SelenideElement title = $(".title");
    @Getter
    private final SelenideElement cartBadge = $(".shopping_cart_badge");
    private final SelenideElement sortDropdown = $(".product_sort_container");

    private final ElementsCollection productImages = $$("img.inventory_item_img");
    private final ElementsCollection items = $$(".inventory_item");


    @Step("Получить ссылки на все картинки")
    public List<String> getProductImagesSource() {

        productImages.shouldHave(sizeGreaterThan(0));
        return productImages.attributes("src");

    }

    @Step("Получить карточки товаров")
    public List<ProductCard> getProductCards() {

        items.shouldHave(sizeGreaterThan(0));

        List<ProductCard> cards = new ArrayList<>();
        for (var item : items) {
            cards.add(new ProductCard(item));
        }
        return cards;

    }

    @Step("Выйти из системы")
    public LoginPage logout() {

        $("#react-burger-menu-btn").click();
        $("#logout_sidebar_link").click();
        return new LoginPage();

    }

    private ProductCard getCardAt(int index) {

        return new ProductCard(items.get(index));

    }

    public SelenideElement getButtonAt(int index) {

        return getCardAt(index).addToCartButton();

    }

    public String getProductNameAt(int index) {

        return getCardAt(index).title().text();

    }

    public String getProductPriceAt(int index) {

        return getCardAt(index).price().text();

    }

    public String getProductDescAt(int index) {

        return getCardAt(index).description().text();

    }

    @Step("Нажать кнопку добавления в корзину у товара с номером {index}")
    public ProductPage addToCart(int index) {

        getButtonAt(index).click();
        return this;

    }

    @Step("Убрать из корзины товар с номером {index}")
    public ProductPage removeFromCart(int index) {

        getButtonAt(index).click();
        return this;

    }

    @Step("Открыть корзину")
    public CartPage openCart() {

        $(".shopping_cart_link").click();
        return new CartPage();

    }
}
