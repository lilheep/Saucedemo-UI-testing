package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    @Getter
    private final ElementsCollection items = $$(".cart_item");

    public ElementsCollection getNames() {

        return $$(".cart_item .inventory_item_name");

    }

    public SelenideElement getTitle() {

        return $(".title");

    }

    public SelenideElement getItemAt(int index) {

        return items.get(index);

    }

    public SelenideElement getNameItemAt(int index) {

        return getItemAt(index).$(".inventory_item_name");

    }

    public SelenideElement getDescriptionItemAt(int index) {

        return getItemAt(index).$(".inventory_item_desc");

    }

    public SelenideElement getPriceItemAt(int index) {

        return getItemAt(index).$(".inventory_item_price");

    }

    public SelenideElement getQuantityItemAt(int index) {

        return getItemAt(index).$(".cart_quantity");

    }

    @Step("Удалить из корзины товар с номером {index}")
    public CartPage removeFromCart(int index) {

        getItemAt(index).$("button").click();
        return this;

    }

    @Step("Вернуться к покупкам")
    public ProductPage continueShopping() {

        $("#continue-shopping").click();
        return new ProductPage();

    }

    @Step("Перейти к оформлению заказа")
    public CheckoutInfoPage checkout() {

        $("#checkout").click();
        return new CheckoutInfoPage();

    }

}
