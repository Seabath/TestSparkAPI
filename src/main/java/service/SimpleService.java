package service;

import dao.SimpleDAO;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SimpleService<T extends Serializable> {

    private static final Logger logger = LogManager.getLogger(SimpleService.class);

    private final SimpleDAO<T> tSimpleDAO;

    public SimpleService(SimpleDAO<T> simpleDAO) {
        this.tSimpleDAO = simpleDAO;
    }


    public T get(Long id) {
        return this.executeQuery(session -> tSimpleDAO.find(session, id));
    }

    public List<T> getAll() {
        return this.executeQuery(tSimpleDAO::findAll);
    }

    public T create(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.save(session, entity));
    }

    public T createOrUpdate(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.saveOrUodate(session, entity));
    }

    public T delete(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.delete(session, entity));
    }

    public T delete(Long id) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.delete(session, tSimpleDAO.find(session, id)));
    }


    protected <V> V executeQuery(Function<Session, V> callable) {
        V retValue;
        try (Session session = tSimpleDAO.openSession()) {
            retValue = callable.apply(session);
        } catch (Exception e) {
            logger.error("Error while getting into the database", e);
            return null;
        }

        return retValue;
    }

    protected <V> V executeQueryWithTransaction(Function<Session, V> callable) {
        V retValue;
        Transaction transaction = null;
        try (Session session = tSimpleDAO.openSession()) {
            transaction = session.beginTransaction();
            retValue = callable.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error while getting into the database", e);
            return null;
        }

        return retValue;
    }
}
