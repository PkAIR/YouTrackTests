package basetests;

import org.junit.jupiter.api.AfterEach;
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
        openUsersPage();
    }

    @AfterEach
    public void afterEach() throws InterruptedException {
        logOutUser();
    }
}
