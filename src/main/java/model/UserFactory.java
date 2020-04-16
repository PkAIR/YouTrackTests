package model;

import com.github.javafaker.Faker;

import java.util.ArrayList;

public class UserFactory {
    private static User getValidUser(boolean mandatoryOnly) {
        Faker faker = new Faker();
        ArrayList<UserGroup> defaultGroups = new ArrayList<UserGroup>() {
            {
                add(UserGroup.AllUsers);
                add(UserGroup.NewUsers);
            }
        };

        if (mandatoryOnly) {
            return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                    "123", "", "", "", defaultGroups);
        } else {
            return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                    "test",
                    faker.name().fullName(),
                    String.format("%s@test.com", faker.gameOfThrones().character()),
                    "test",
                    defaultGroups);
        }
    }

    public static User getUserAllFlds() {
        return getValidUser(false);
    }

    public static User getUserMandatoryFldsOnly() {
        return getValidUser(true);
    }
}
