package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaymentTest {
    String validCardNumber = DataHelper.getApprovedCard().getCardNumber();
    String declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
    String unknownCardNumber = DataHelper.getUnknownCard().getCardNumber();
    String validMonth = DataHelper.getValidMonth();
    String validYear = DataHelper.getValidYear(1);
    String validName = DataHelper.getValidName();
    String validCode = DataHelper.getNumber(3);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    @DisplayName("Отправка формы по данным активной карты")
    void shouldHappyPath() {
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifySuccessVisibility();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка формы по данным неактивной карты")
    void shouldSadPath() {
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(declinedCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyErrorVisibility();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка формы по данным неизвестной карты")
    void shouldReturnFailWithUnknownCard() {
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(unknownCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyErrorVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка пустой формы")
    void shouldEmptyPath() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var emptyMonth = DataHelper.getEmptyValue();
        var emptyYear = DataHelper.getEmptyValue();
        var emptyName = DataHelper.getEmptyValue();
        var emptyCode = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        paymentpage.verifyEmptyCardNumberVisibility();
        paymentpage.verifyEmptyMonthVisibility();
        paymentpage.verifyEmptyYearVisibility();
        paymentpage.verifyEmptyNameVisibility();
        paymentpage.verifyEmptyCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым номером карты")
    void shouldReturnErrorWithEmptyCardNumber() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(emptyCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyEmptyCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым значением месяца")
    void shouldReturnErrorWithEmptyMonth() {
        var emptyMonth = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, emptyMonth, validYear, validName, validCode);
        paymentpage.verifyEmptyMonthVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым значением года")
    void shouldReturnErrorWithEmptyYear() {
        var emptyYear = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, emptyYear, validName, validCode);
        paymentpage.verifyEmptyYearVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым именем владельца")
    void shouldReturnErrorWithEmptyName() {
        var emptyName = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, emptyName, validCode);
        paymentpage.verifyEmptyNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с пустым кодом")
    void shouldReturnErrorWithEmptyCode() {
        var emptyCode = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, emptyCode);
        paymentpage.verifyEmptyCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с номером карты, заполненным кириллицей")
    void shouldReturnErrorWithCyrillicCardNumber() {
        var cyrillicCardNumber = DataHelper.getNameOfCyrillic();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(cyrillicCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с номером карты, заполненным латиницей")
    void shouldReturnErrorWithLatinCardNumber() {
        var latinCardNumber = DataHelper.getValidName();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(latinCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с номером карты, заполненным спецсимволами")
    void shouldReturnErrorWithSymbolsCardNumber() {
        var symbolCardNumber = DataHelper.getSymbols();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(symbolCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с номером карты, заполненным не полностью")
    void shouldReturnErrorWithShortCardNumber() {
        var shortCardNumber = DataHelper.getNumber(15);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(shortCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку с номером карты, заполненным с лишними цифрами")
    void shouldReturnErrorWithLongCardNumber() {
        var longCardNumber = DataHelper.getNumber(17);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(longCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным кириллицей")
    void shouldReturnErrorWithCyrillicMonth() {
        var cyrillicMonth = DataHelper.getNameOfCyrillic();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, cyrillicMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным латиницей")
    void shouldReturnErrorWithLatinMonth() {
        var latinMonth = DataHelper.getValidName();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, latinMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным спецсимволами")
    void shouldReturnErrorWithSymbolsMonth() {
        var symbolMonth = DataHelper.getSymbols();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, symbolMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным не полностью")
    void shouldReturnErrorWithShortMonth() {
        var shortMonth = DataHelper.getNumber(1);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, shortMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным с лишними цифрами")
    void shouldReturnErrorWithLongMonth() {
        var longMonth = DataHelper.getNumber(3);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, longMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением месяца, заполненным двузначным значением более 12")
    void shouldReturnErrorWithErrorMonth() {
        var errorMonth = "13";
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, errorMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным кириллицей")
    void shouldReturnErrorWithCyrillicYear() {
        var cyrillicYear = DataHelper.getNameOfCyrillic();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, cyrillicYear, validName, validCode);
        paymentpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным латиницей")
    void shouldReturnErrorWithLatinYear() {
        var latinYear = DataHelper.getValidName();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, latinYear, validName, validCode);
        paymentpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным спецсимволами")
    void shouldReturnErrorWithSymbolsYear() {
        var symbolYear = DataHelper.getSymbols();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, symbolYear, validName, validCode);
        paymentpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным не полностью")
    void shouldReturnErrorWithShortYear() {
        var shortYear = DataHelper.getNumber(1);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, shortYear, validName, validCode);
        paymentpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным с лишними цифрами")
    void shouldReturnErrorWithLongYear() {
        var longYear = "333";
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, longYear, validName, validCode);
        paymentpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным более чем на 5 лет вперед")
    void shouldReturnErrorWithOverFiveYear() {
        var overFiveYear = DataHelper.getValidYear(6);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, overFiveYear, validName, validCode);
        paymentpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением года, заполненным в прошлом")
    void shouldReturnErrorWithLastYear() {
        var lastYear = DataHelper.getValidYear(-1);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, lastYear, validName, validCode);
        paymentpage.verifyExpiredValidityPeriodVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Владелец, заполненным кириллицей")
    void shouldReturnErrorWithCyrillicName() {
        var cyrillicName = DataHelper.getNameOfCyrillic();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, cyrillicName, validCode);
        paymentpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Владелец, заполненным цифрами")
    void shouldReturnErrorWithNumberName() {
        var numberName = DataHelper.getNumber(5);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, numberName, validCode);
        paymentpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Владелец, заполненным спецсимволами")
    void shouldReturnErrorWithSymbolName() {
        var symbolName = DataHelper.getSymbols();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, symbolName, validCode);
        paymentpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Владелец, заполненным только фамилией")
    void shouldReturnErrorWithSurname() {
        var surname = DataHelper.getValidSurname();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, surname, validCode);
        paymentpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением поля Владелец, заполненным одной буквой")
    void shouldReturnErrorWithOneLetterName() {
        var oneLetterName = "a";
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, oneLetterName, validCode);
        paymentpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением кода, заполненным кириллицей")
    void shouldReturnErrorWithCyrillicCode() {
        var cyrillicCode = DataHelper.getNameOfCyrillic();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, cyrillicCode);
        paymentpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением кода, заполненным латиницей")
    void shouldReturnErrorWithLatinCode() {
        var latinCode = DataHelper.getValidName();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, latinCode);
        paymentpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением кода, заполненным спецсимволами")
    void shouldReturnErrorWithSymbolsCode() {
        var symbolCode = DataHelper.getSymbols();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, symbolCode);
        paymentpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением кода, заполненным не полностью")
    void shouldReturnErrorWithShortCode() {
        var shortCode = DataHelper.getNumber(1);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, shortCode);
        paymentpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен вернуть ошибку со значением кода, заполненным с лишними цифрами")
    void shouldReturnErrorWithLongCode() {
        var longCode = DataHelper.getNumber(4);
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, longCode);
        paymentpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getPaymentStatus());
    }
}