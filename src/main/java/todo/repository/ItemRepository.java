package todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import todo.model.Item;

import java.util.Collection;
import java.util.List;

public class ItemRepository implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    private static ItemRepository instance;

    private ItemRepository() {
    }

    public static synchronized ItemRepository getInstance() {
        if(instance == null) {
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
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Collection<Item> findAll() {

        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Collection<Item> findByDoneAll(Boolean done) {

        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item where done=:done");
        query.setParameter("done", done);
        List<Item> result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public boolean delete(Integer id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = new Item(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean replace(Integer id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        item.setId(id);
        session.update(item);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    public void close() throws Exception {
        if (sf.isOpen()) {
            sf.close();
        }
    }
}
