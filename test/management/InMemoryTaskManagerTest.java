package management;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void initManager() {
        manager = getTaskManager();
    }

    @Override
    InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }
}
