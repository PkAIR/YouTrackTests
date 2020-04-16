# YouTrackTests

## Prerequisite:
- Gradle;
- JDK

## Libs used
- Selenide;
- Junit5;
- faker

## Usefull commands

To run all tests in chrome use:

- `gradle :cleanTest :test` 

The same way for firefox:

- `gradle :cleanTest :test -Dselenide.browser=firefox`

To execute a specific test use *--tests* keyword
- `gradle :cleanTest :test --tests "create.user.tests.admin.pages.NegativeUsersTests"`
