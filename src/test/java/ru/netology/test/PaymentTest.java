package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    String validCardNumber = DataHelper.getApprovedCard().getCardNumber();
    String declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
    String validMonth = DataHelper.getValidMonth();
    String validYear = DataHelper.getValidYear(1);
    String validName = DataHelper.getValidName();
    String validCode = DataHelper.getNumber(3);

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    void shouldHappyPath() {
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifySuccessVisibility();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldSadPath() {
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(declinedCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyErrorVisibility();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test // поменять результат на "как должно работать" (вместо InvalidFormat - EmptyField), завести баг!
    void shouldEmptyPath() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var emptyMonth = DataHelper.getEmptyValue();
        var emptyYear = DataHelper.getEmptyValue();
        var emptyName = DataHelper.getEmptyValue();
        var emptyCode = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        paymentpage.verifyEmptyFieldVisibility();
        paymentpage.verifyInvalidFormatVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

    @Test // поменять результат на "как должно работать" (вместо InvalidFormat - EmptyField), завести баг!
    void shouldReturnWithEmptyCardNumber() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(emptyCardNumber, validMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

    @Test // поменять результат на "как должно работать" (вместо InvalidFormat - EmptyField), завести баг!
    void shouldReturnWithEmptyMonth() {
        var emptyMonth = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, emptyMonth, validYear, validName, validCode);
        paymentpage.verifyInvalidFormatVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

    @Test // поменять результат на "как должно работать" (вместо InvalidFormat - EmptyField), завести баг!
    void shouldReturnWithEmptyYear() {
        var emptyYear = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, emptyYear, validName, validCode);
        paymentpage.verifyInvalidFormatVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

    @Test
    void shouldReturnWithEmptyName() {
        var emptyName = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, emptyName, validCode);
        paymentpage.verifyEmptyFieldVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

    @Test // поменять результат на "как должно работать" (вместо InvalidFormat - EmptyField), завести баг!
    void shouldReturnWithEmptyCode() {
        var emptyCode = DataHelper.getEmptyValue();
        var paymentpage = new PaymentPage();
        paymentpage.cleanPaymentForm();
        paymentpage.enterInputs(validCardNumber, validMonth, validYear, validName, emptyCode);
        paymentpage.verifyInvalidFormatVisibility();
        assertEquals(null, SQLHelper.getPaymentStatus());
    }

}
