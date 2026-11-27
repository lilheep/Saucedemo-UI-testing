package pages;

import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.DownloadOptions.using;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.files.FileFilters.withExtension;

public class CheckoutCompletePage {

    public SelenideElement getHeader() {

        return $(".complete-header");

    }

    @Step("Вернуться к каталогу товаров")
    public ProductPage backHome() {

        $("#back-to-products").click();
        return new ProductPage();

    }

    @Step("Сгенерировать счет заказа в формате .pdf")
    public File downloadReceipt() {

        return $("#generate-pdf-order").download(
                using(FileDownloadMode.CDP).withFilter(withExtension("pdf")));

    }

}
