package dao;

import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class SimpleDAO<T extends Serializable> {

    private static final Logger logger = LogManager.getLogger(SimpleDAO.class);
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private static SessionFactory sessionFactory;

    private Class<T> clazz;

    public SimpleDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T find(Session session, long id) {
        return session.get(clazz, id);
    }

    public List<T> findAll(Session session) {
        return session
            .createQuery("from " + clazz.getName(), clazz).list();
    }

    public void save(Session session, T entity) {
        session.persist(entity);
    }

    public void saveOrUodate(Session session, T entity) {
        session.saveOrUpdate(entity);
    }

    public void delete(Session session, T entity) {
        session.delete(entity);
    }

    public void deleteById(Session session, long id) {
        final T entity = find(session, id);
        delete(session, entity);
    }

    public final Session openSession() {
        return getSessionFactory().openSession();
    }

    public final Session openSessionWithTransaction() {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        return session;
    }

    public final void closeSession(Session session) {
        session.close();
    }

    public final void closeSessionWithTransaction(Session session) {
        session.getTransaction().commit();
        session.close();
    }


    private SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder configure = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml");
                configure.applySetting("hibernate.connection.url", "jdbc:postgresql:" + DB_URL);
                configure.applySetting("hibernate.connection.username", DB_USER);
                configure.applySetting("hibernate.connection.password", DB_PASSWORD);
                ServiceRegistry serviceRegistry = configure.build();

                Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Throwable t) {
                logger.error("Could initialize hibernate.");
                throw new ExceptionInInitializerError(t);
            }
        }

        return sessionFactory;
    }
}
