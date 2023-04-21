package com.open_squad.openplan.model;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class OrderDetail.
 * @see com.open_squad.openplan.model.OrderDetail
 * @author Hibernate Tools
 */
public class OrderDetailHome {

    private static final Log log = LogFactory.getLog(OrderDetailHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(OrderDetail transientInstance) {
        log.debug("persisting OrderDetail instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(OrderDetail instance) {
        log.debug("attaching dirty OrderDetail instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(OrderDetail instance) {
        log.debug("attaching clean OrderDetail instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(OrderDetail persistentInstance) {
        log.debug("deleting OrderDetail instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public OrderDetail merge(OrderDetail detachedInstance) {
        log.debug("merging OrderDetail instance");
        try {
            OrderDetail result = (OrderDetail) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public OrderDetail findById(int id) {
        log.debug("getting OrderDetail instance with id: " + id);
        try {
            OrderDetail instance = (OrderDetail) sessionFactory.getCurrentSession().get("com.open_squad.openplan.model.OrderDetail", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<OrderDetail> findByExample(OrderDetail instance) {
        log.debug("finding OrderDetail instance by example");
        try {
            List<OrderDetail> results = (List<OrderDetail>) sessionFactory.getCurrentSession().createCriteria("com.open_squad.openplan.model.OrderDetail").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
