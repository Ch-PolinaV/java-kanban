package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> nodeIds = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

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
        removeNode(nodeIds.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> tasksHistory = new ArrayList<>();
        Node<Task> curNode = head;
        while (curNode != tail) {
            tasksHistory.add(curNode.data);
            curNode = curNode.next;
            tasksHistory.remove(null);
        }
        tasksHistory.add(curNode.data);
        return tasksHistory;
    }

    private void linkLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(null, task, oldTail);
        tail = newNode;
        nodeIds.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            node.data = null;
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
                if (node.prev != null && node.next != null) {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
            }
        }
    }
}

class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}