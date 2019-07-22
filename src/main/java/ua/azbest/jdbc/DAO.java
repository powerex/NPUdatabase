package ua.azbest.jdbc;

public interface DAO<Entity, Key> {
    int create(Entity model);
    Entity read(Key key);
    int update(Entity model);
    int delete(Entity model);
}