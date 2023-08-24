package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker faker1 = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String cardStatus;
    }

    public static CardInfo getApprovedCard() {

        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getDeclinedCard() {

        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static CardInfo getUnknownCard() {

        return new CardInfo("4444 4444 4444 4444", "");
    }

    public static String getValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getValidYear(int year) {
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getValidName() {
        return faker.name().fullName();
    }

    public static String getValidSurname() {
        return faker.name().lastName();
    }

    public static String getNameOfCyrillic() {
        return faker1.name().fullName();
    }

    public static String getNumber(int number) {
        return faker.number().digits(number);
    }

    public static String getSymbols() {
        return "!@#$%";
    }

    public static String getEmptyValue() {
        return "";
    }
}