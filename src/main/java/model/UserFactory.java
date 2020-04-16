package model;

import com.github.javafaker.Faker;

import java.util.ArrayList;

public class UserFactory {
    private enum typeOfUser {
        selfRegistration,
        mandatoryFldsOnly,
        allFlds
    }

    private static User getValidUser(typeOfUser typeOfUser) {
        Faker faker = new Faker();
        ArrayList<UserGroup> defaultGroups = new ArrayList<UserGroup>() {
            {
                add(UserGroup.AllUsers);
                add(UserGroup.NewUsers);
            }
        };

        switch (typeOfUser)
        {
            case allFlds: {
                return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                "test",
                    faker.name().fullName(),
                        String.format("%s@test.com", faker.gameOfThrones().character()),
                        "test",
                        defaultGroups);
            }
            case selfRegistration: {
                return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                        "test",
                        faker.name().fullName(),
                        String.format("%s@test.com", faker.gameOfThrones().character()),
                        "",
                        defaultGroups);
            }
            default:
            case mandatoryFldsOnly: {
                return new User((faker.cat().name() + faker.artist().name()).replaceAll("\\s+",""),
                        "123", "", "", "", defaultGroups);
            }
        }
    }

    public static User getSelfRegistrationUser() {
        return getValidUser(typeOfUser.selfRegistration);
    }

    public static User getUserMandatoryFldsOnly() {
        return getValidUser(typeOfUser.mandatoryFldsOnly);
    }

    public static User getUserAllFlds() {
        return getValidUser(typeOfUser.allFlds);
    }
}
