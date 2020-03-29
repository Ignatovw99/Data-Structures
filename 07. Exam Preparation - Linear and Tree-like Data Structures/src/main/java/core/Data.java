package core;

import interfaces.Entity;
import interfaces.Repository;
import model.Invoice;
import model.StoreClient;
import model.User;

import java.util.*;
import java.util.stream.Collectors;

public class Data implements Repository {

    private Entity root;

    private Queue<Entity> data;

    public Data() {
        root = null;
        data = new PriorityQueue<>();
    }

    public Data(Data other) {
        root = other.root;
        data = new PriorityQueue<>(other.data);
    }

    @Override
    public void add(Entity entity) {
        if (root == null) {
            root = entity;
        } else {
            getById(entity.getParentId())
                    .addChild(entity);
        }
        data.offer(entity);
    }

    @Override
    public Entity getById(int id) {
        Deque<Entity> queue = new ArrayDeque<>();

        if (root != null) {
            queue.offer(root);
        }

        while (!queue.isEmpty()) {
            Entity current = queue.poll();

            if (current.getId() == id) {
                return current;
            }

            for (Entity entity : current.getChildren()) {
                queue.offer(entity);
            }
        }

        return null;
    }

    @Override
    public List<Entity> getByParentId(int id) {
        Entity parent = getById(id);
        return parent == null ? new ArrayList<>() : parent.getChildren();
    }

    @Override
    public List<Entity> getAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Repository copy() {
        return new Data(this);
    }

    @Override
    public List<Entity> getAllByType(String type) {
        List<String> validClasses = List.of(Invoice.class.getSimpleName(), StoreClient.class.getSimpleName(), User.class.getSimpleName());
        if (!validClasses.contains(type)) {
            throw new IllegalArgumentException("Illegal type: " + type);
        }

        return data.stream()
                .filter(entity -> entity.getClass().getSimpleName().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public Entity peekMostRecent() {
        if (size() == 0) {
            throw new IllegalStateException("Operation on empty data");
        }
        return data.peek();
    }

    @Override
    public Entity pollMostRecent() {
        if (size() == 0) {
            throw new IllegalStateException("Operation on empty data");
        }
        Entity entityToPoll = data.poll();
        Entity parentEntity = getById(entityToPoll.getParentId());
        parentEntity.getChildren().remove(entityToPoll);

        return entityToPoll;
    }

    @Override
    public int size() {
        return data.size();
    }
}
