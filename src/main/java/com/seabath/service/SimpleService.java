package com.seabath.service;

import com.seabath.dao.SimpleDAO;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Generic service that interact with the DAO to manipulate the database.
 *
 * @param <T> Type of object you want to put / get from the database.
 */
public class SimpleService<T extends Serializable> {

    private static final Logger logger = LogManager.getLogger(SimpleService.class);

    private final SimpleDAO<T> tSimpleDAO;

    public SimpleService(SimpleDAO<T> simpleDAO) {
        this.tSimpleDAO = simpleDAO;
    }


    /**
     * Gets an entity with given id.
     *
     * @param id Id of the entity you want to retrieve.
     * @return Entity matching given id. Null if error happened.
     */
    public T get(Long id) {
        return this.executeQuery(session -> tSimpleDAO.find(session, id));
    }

    /**
     * Gets a list containing all entities from a table.
     *
     * @return All entities from a table. Null if error happened.
     */
    public List<T> getAll() {
        return this.executeQuery(tSimpleDAO::findAll);
    }

    /**
     * Creates a new entry into the database with given entity.
     *
     * @param entity Entity to put into the database.
     * @return Updated entity with its id filled. Null if error happened.
     */
    public T create(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.save(session, entity));
    }

    /**
     * Creates or updates an entry into the database with given entity.
     *
     * @param entity Entity to put or update into the database.
     * @return Updated entity with its id filled. Null if error happened.
     */
    public T createOrUpdate(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.saveOrUpdate(session, entity));
    }

    /**
     * Deletes an entry from the database.
     *
     * @param entity Entity to put or update into the database.
     * @return Deleted entity. Null if error happened.
     */
    public T delete(T entity) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.delete(session, entity));
    }

    /**
     * Deletes an entry from the database with given id;
     *
     * @param id Id of the entity to delete from the database.
     * @return Deleted entity. Null if error happened.
     */
    public T delete(Long id) {
        return this.executeQueryWithTransaction(session ->
            tSimpleDAO.delete(session, tSimpleDAO.find(session, id)));
    }


    /**
     * Opens a session from the com.seabath.dao and executes series of queries from the session.
     *
     * @param function Function containing queries to execute.
     * @param <V>      Type of return of given function.
     * @return Returns what given function supposed to return.
     */
    protected <V> V executeQuery(Function<Session, V> function) {
        V retValue;
        try (Session session = tSimpleDAO.openSession()) {
            retValue = function.apply(session);
        } catch (Exception e) {
            logger.error("Error while getting into the database", e);
            return null;
        }

        return retValue;
    }


    /**
     * Opens a session with transaction from the com.seabath.dao and executes series of queries from the
     * session.
     *
     * @param function Function containing queries to execute.
     * @param <V>      Type of return of given function.
     * @return Returns what given function supposed to return.
     */
    protected <V> V executeQueryWithTransaction(Function<Session, V> function) {
        V retValue;
        Transaction transaction = null;
        try (Session session = tSimpleDAO.openSession()) {
            transaction = session.beginTransaction();
            retValue = function.apply(session);
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
