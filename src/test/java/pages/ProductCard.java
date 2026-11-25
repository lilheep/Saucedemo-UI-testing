package pages;

import com.codeborne.selenide.SelenideElement;
import lombok.AllArgsConstructor;

import static com.codeborne.selenide.Selenide.$;

@AllArgsConstructor
public class ProductCard {

    private final SelenideElement root;

    public SelenideElement title()
    {
        return root.$(".inventory_item_name");
    }

    public SelenideElement description()
    {
        return root.$(".inventory_item_desc");
    }

    public SelenideElement price()
    {
        return root.$(".inventory_item_price");
    }

    public SelenideElement image()
    {
        return root.$("img.inventory_item_img");
    }

    public SelenideElement addToCartButton()
    {
        return root.$("button");
    }

}
