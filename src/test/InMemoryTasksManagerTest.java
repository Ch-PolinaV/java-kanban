package test;

import manager.InMemoryTaskManager;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
