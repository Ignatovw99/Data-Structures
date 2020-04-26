package shared;

import model.Task;

import java.util.List;

public interface Scheduler {
    void add(Task task);
    Task process();
    Task peek();
    Boolean contains(Task task);
    int size();
    Boolean remove(Task task);
    Boolean remove(int id);
    void insertBefore(int id, Task task);
    void insertAfter(int id, Task task);
    void clear();
    Task[] toArray();
    void reschedule(Task first, Task second);
    List<Task> toList();
    void reverse();
    Task find(int id);
    Task find(Task task);
}
