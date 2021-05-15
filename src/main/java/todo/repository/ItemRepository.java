package todo.repository;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import todo.model.IdAble;
import todo.model.Item;
import todo.model.User;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.function.Function;

public class ItemRepository implements AutoCloseable {
    private static final Logger LOG = LogManager.getLogger(ItemRepository.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    public final static ItemRepository instance = new ItemRepository();

    private ItemRepository() {
    }

    public static ItemRepository getInstance() {
        return instance;
    }

    public <T extends IdAble> T save(T item) {
        return item.getId() == null ? add(item) : replace(item);
    }

    public <T> T add(T item) {
        return execute(session -> {
            session.save(item);
            return item;
        });
    }

    public <T> T findById(Class<T> tClass, Integer id) {
        return execute(session -> session.get(tClass, id));
    }

    public <T> List<T> findAll(Class<T> tClass) {
        String queryString = String.format("from %s ORDER BY id", tClass.getName());
        return execute(session -> session.createQuery(queryString).list());
    }

    public List<Item> findByDoneAll(Boolean done) {
        return execute(session -> {
            Query query = session.createQuery("from Item i where i.done=:done order by i.id");
            query.setParameter("done", done);
            return query.list();
        });
    }

    public User findByEmail(String email) {
        try {
            return (User) execute(session -> {
                Query query = session.createQuery("from User u where u.email=:email order by u.id");
                query.setParameter("email", email);
                return query.getSingleResult();
            });
        } catch (final NoResultException e) {
            LOG.error(e);
            return null;
        }
    }

    public <T> boolean delete(T item) {
        return execute(session -> {
            session.delete(item);
            return true;
        });
    }

    public <T> T replace(T item) {
        return execute(session -> {
            session.update(item);
            return item;
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
            LOG.error(e);
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
