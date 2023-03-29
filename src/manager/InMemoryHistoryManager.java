package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeIds = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodeIds.containsKey(task.getId())) {
                remove(task.getId());
            }
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (!nodeIds.isEmpty()) {
            if (nodeIds.get(id) != null) {
                removeNode(nodeIds.get(id));
            }
        }
    }

    @Override
    public List<Integer> getHistory() {
        return getTaskId();
    }

    private List<Integer> getTaskId() {
        List<Integer> taskId = new ArrayList<>();
        Node curNode = head;
        while (curNode != null) {
            taskId.add(curNode.data.getId());
            curNode = curNode.next;
        }
        return taskId;
    }

    private void linkLast(Task task) {
        if (task != null) {
            Node oldTail = tail;
            Node newNode = new Node(oldTail, task, null);
            tail = newNode;
            nodeIds.put(task.getId(), newNode);
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
        }
    }

    private void removeNode(Node node) {
        if (head == node && tail == node) {
            head = null;
            tail = null;
        } else if (head == node) {
            head = head.next;
            head.prev = null;
        } else if (tail == node) {
            tail = tail.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}


class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}