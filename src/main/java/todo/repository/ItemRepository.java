package todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import todo.model.Item;

import java.util.Collection;
import java.util.function.Function;

public class ItemRepository implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    private static ItemRepository instance;

    private ItemRepository() {
    }

    public static synchronized ItemRepository getInstance() {
        if (instance == null) {
            instance = new ItemRepository();
        }
        return instance;
    }

    public Item save(Item item) {
        if (item.getId() == null) {
            add(item);
        } else {
            replace(item.getId(), item);
        }
        return item;
    }

    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();

        return item;
    }

    public Item findById(Integer id) {
        return execute(session -> session.get(Item.class, id));
    }

    public Collection<Item> findAll() {
        return execute(session -> session.createQuery("from Item").list());
    }

    public Collection<Item> findByDoneAll(Boolean done) {

        return execute(session -> {
            Query query = session.createQuery("from Item where done=:done");
            query.setParameter("done", done);
            return query.list();
        });
    }

    public boolean delete(Integer id) {
        return execute(session -> {
            Item item = new Item(id);
            session.delete(item);
            return true;
        });
    }

    public boolean replace(Integer id, Item item) {
        return execute(session -> {
            item.setId(id);
            session.update(item);
            return true;
        });
    }

    private <T> T execute(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void close() throws Exception {
        if (sf.isOpen()) {
            sf.close();
        }
    }
}
