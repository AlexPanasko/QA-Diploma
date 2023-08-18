package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CreditPage {
    private final SelenideElement headerCredit = $x(".//h3[contains(text(), 'Кредит')]");
    private final SelenideElement buyCreditButton = $x(".//span[text()='Купить в кредит']");
    private final SelenideElement cardInput = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthInput = $("[placeholder='08']");
    private final SelenideElement yearInput = $("[placeholder='22']");
    private final SelenideElement nameInput = $x(".//span[contains(text(), 'Владелец')]/..//input[@class='input__control']");
    private final SelenideElement codeInput = $("[placeholder='999']");
    private final SelenideElement buttonForm = $x(".//form//button");

    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");
    private final SelenideElement emptyFieldNotification = $x(".//span[text()='Поле обязательно для заполнения']");
    private final SelenideElement invalidFormatNotification = $x(".//span[text()='Неверный формат']");
    private final SelenideElement invalidValidityPeriodNotification = $x(".//span[text()='Неверно указан срок действия карты']");

    public CreditPage() {
        buyCreditButton.click();
        headerCredit
                .shouldBe(visible)
                .shouldHave(text("Кредит по данным карты"));
    }

    public void cleanCreditForm() {
        cardInput.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        monthInput.doubleClick().sendKeys(Keys.DELETE);
        yearInput.doubleClick().sendKeys(Keys.DELETE);
        nameInput.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        codeInput.doubleClick().sendKeys(Keys.DELETE);
    }

    public void enterCreditInputs(String card, String month, String year, String name, String code) {
        cardInput.setValue(card);
        monthInput.setValue(month);
        yearInput.setValue(year);
        nameInput.setValue(name);
        codeInput.setValue(code);
        buttonForm.click();
    }

    public void verifySuccessVisibility() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void verifyErrorVisibility() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void verifyEmptyFieldVisibility() {
        emptyFieldNotification.shouldBe(visible);
    }

    public void verifyInvalidFormatVisibility() {
        invalidFormatNotification.shouldBe(visible);
    }

    public void verifyInvalidValidityPeriodVisibility() {
        invalidValidityPeriodNotification.shouldBe(visible);
    }
}