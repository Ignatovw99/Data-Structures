package core;

import model.Task;
import shared.Scheduler;

import java.util.*;

public class ProcessScheduler implements Scheduler {

    private Deque<Task> deque;

    private List<Task> list;

    public ProcessScheduler() {
        deque = new ArrayDeque<>();
        list = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        deque.offer(task);
        list.add(task);
    }

    @Override
    public Task process() {
        Task task = deque.poll();
        list.remove(task);
        return task;
    }

    @Override
    public Task peek() {
        return deque.peek();
    }

    @Override
    public Boolean contains(Task task) {
        return deque.contains(task);
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public Boolean remove(Task task) {
        Task taskToRemove = findById(task.getId());
        if (taskToRemove == null) {
            throw new IllegalArgumentException();
        }
        list.remove(taskToRemove);
        return deque.remove(taskToRemove);
    }

    @Override
    public Boolean remove(int id) {
        Task taskToRemove = findById(id);
        if (taskToRemove == null) {
            throw new IllegalArgumentException();
        }
        list.remove(taskToRemove);
        return deque.remove(taskToRemove);
    }

    private Task findById(int id) {
        for (Task datum : deque) {
            if (datum.getId() == id) {
                return datum;
            }
        }
        return null;
    }

    @Override
    public void insertBefore(int id, Task task) {
        int index = indexOf(id);
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        list.add(index, task);
        deque = new ArrayDeque<>(list);
    }

    private int indexOf(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insertAfter(int id, Task task) {
        int index = indexOf(id);
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        if (index + 1 >= size()) {
            list.add(task);
        } else {
            list.add(index + 1, task);
        }
        deque = new ArrayDeque<>(list);
    }

    @Override
    public void clear() {
        deque.clear();
        list.clear();
    }

    @Override
    public Task[] toArray() {
        Task[] arr = new Task[list.size()];
        list.toArray(arr);
        return arr;
    }

    @Override
    public void reschedule(Task first, Task second) {
        int index = list.indexOf(first);
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        int index2 = list.indexOf(second);
        if (index2 == -1) {
            throw new IllegalArgumentException();
        }

        Collections.swap(list, index, index2);
        deque = new ArrayDeque<>(list);
    }

    @Override
    public List<Task> toList() {
        return list;
    }

    @Override
    public void reverse() {
        Collections.reverse(list);
        deque = new ArrayDeque<>(list);
    }

    @Override
    public Task find(int id) {
        Task task = findById(id);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        return task;
    }

    @Override
    public Task find(Task task) {
        Task t = findById(task.getId());
        if (t == null) {
            throw new IllegalArgumentException();
        }
        return t;
    }
}
