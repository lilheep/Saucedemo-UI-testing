package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutInfoPage {

    private final SelenideElement firstName = $("#first-name");
    private final SelenideElement lastName = $("#last-name");
    private final SelenideElement postalCode = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");

    @Getter
    private final SelenideElement errorMessage = $("[data-test='error']");

    @Step("Заполнить необходимые данные и продолжить: {firstName} {lastName}, {postalCode}")
    public CheckoutOverviewPage fillInfo(String firstName,
                                         String lastName,
                                         String postalCode) {

        this.firstName.setValue(firstName);
        this.lastName.setValue(lastName);
        this.postalCode.setValue(postalCode);

        continueButton.click();
        return new CheckoutOverviewPage();

    }

}
