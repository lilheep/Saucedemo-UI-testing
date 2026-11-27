package pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CheckoutOverviewPage {

    public ElementsCollection getItems() {

        return $$(".cart_item .inventory_item_name");

    }

    @Step("Завершить заказ")
    public CheckoutCompletePage finish() {

        $("#finish").click();
        return new CheckoutCompletePage();

    }

}
