package management;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    private static class Node {
        private Node prev;
        private Task item;
        private Node next;

        public Node(Node prev, Task item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    // Реализация данного метода "подсмотрена" в классе LinkedList, разобрана и понята
    private void linkLast(Task task) {
        Node l = last;
        Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        history.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) { // Если удаляется единственный элемент списка
            first = null;
            last = null;
        } else if (node.prev == null) { // Если удаляется первый элемент
            first = node.next;
            first.prev = null;
        } else if (node.next == null) { // Если удаляется последний элемент
            last = node.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        history.remove(node.item.getId());
    }

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> listViewedTask = new ArrayList<>();
        Node current = first;
        while (current != null) {
            listViewedTask.add(current.item);
            current = current.next;
        }
        return listViewedTask;
    }
}