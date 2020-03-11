package model;

import com.github.javafaker.Faker;

public class UserFactory {
    private static User getValidUser(boolean mandatoryOnly) {
        Faker faker = new Faker();

        if (mandatoryOnly) {
            return new User(faker.cat().name(),
                    "123", "", "", "");
        } else {
            return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                    "test",
                    faker.name().fullName(),
                    String.format("%s@test.com", faker.gameOfThrones().character()),
                    "test");
        }
    }

    public static User getUserAllFlds() {
        return getValidUser(false);
    }

    public static User getUserMandatoryFldsOnly() {
        return getValidUser(true);
    }
}
