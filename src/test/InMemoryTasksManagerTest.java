package test;

import manager.InMemoryTaskManager;

import java.io.IOException;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTasksManagerTest() throws IOException {
    }

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
