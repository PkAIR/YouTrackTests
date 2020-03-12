package basetests;

import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class BaseDdtTest extends BaseTest {
    @BeforeAll
    public static void beforeAll() throws IOException {
        commonSetup();
        openUsersPage();
    }
}
