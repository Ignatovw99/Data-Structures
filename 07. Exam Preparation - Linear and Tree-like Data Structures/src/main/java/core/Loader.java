package core;

import interfaces.Buffer;
import interfaces.Entity;
import model.BaseEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Loader implements Buffer {

    private List<Entity> data;

    public Loader() {
        data = new ArrayList<>();
    }

    @Override
    public void add(Entity entity) {
        data.add(entity);
    }

    private Entity findEntityWithId(int id) {
        Entity entity = null;
        for (Entity datum : data) {
            if (datum.getId() == id) {
                entity = datum;
                break;
            }
        }
        return entity;
    }

    @Override
    public Entity extract(int id) {
        Entity entity = findEntityWithId(id);
        if (entitiesCount() == 0 || entity == null) {
            return null;
        }
        data.remove(entity);
        return entity;
    }

    @Override
    public Entity find(Entity entity) {
        int entityIndex = data.lastIndexOf(entity);
        if (entityIndex == -1 || entitiesCount() == 0) {
            return null;
        }
        return data.get(entityIndex);
    }

    @Override
    public boolean contains(Entity entity) {
        return data.contains(entity);
    }

    @Override
    public int entitiesCount() {
        return data.size();
    }

    @Override
    public void replace(Entity oldEntity, Entity newEntity) {
        int oldEntityIndex = data.indexOf(oldEntity);
        if (oldEntityIndex == -1) {
            throw new IllegalStateException("Entity not found");
        }
        data.set(oldEntityIndex, newEntity);
    }

    private void ensureIndex(int index) {
        if (index == -1) {
            throw new IllegalStateException("Entities not found");
        }
    }

    @Override
    public void swap(Entity first, Entity second) {
        int firstIndex = data.indexOf(first);
        ensureIndex(firstIndex);
        int secondIndex = data.indexOf(second);
        ensureIndex(secondIndex);

        data.set(firstIndex, second);
        data.set(secondIndex, first);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Entity[] toArray() {
        Entity[] array = new Entity[entitiesCount()];
        data.toArray(array);
        return array;
    }

    @Override
    public List<Entity> retainAllFromTo(BaseEntity.Status lowerBound, BaseEntity.Status upperBound) {
        return data
                .stream()
                .filter(entity -> entity.getStatus().ordinal() >= lowerBound.ordinal() && entity.getStatus().ordinal() <= upperBound.ordinal())
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateAll(BaseEntity.Status oldStatus, BaseEntity.Status newStatus) {
        data = data.stream().peek(entity -> {
            if (entity.getStatus().equals(oldStatus)) {
                entity.setStatus(newStatus);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void removeSold() {
        data = data.stream()
                .filter(entity -> !entity.getStatus().equals(BaseEntity.Status.SOLD))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Entity> iterator() {
        return data.iterator();
    }
}
