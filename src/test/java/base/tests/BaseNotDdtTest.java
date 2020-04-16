package base.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class BaseNotDdtTest extends BaseTest{
    @BeforeAll
    public static void beforeAllSetUp() throws IOException {
        commonSetup();
    }

    @BeforeEach
    public void beforeEachSetup() {
        openUsersPageAsRootUser();
        deleteAllUsers();
    }
}
