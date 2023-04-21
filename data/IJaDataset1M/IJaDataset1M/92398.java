package Entities;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author user
 */
@Stateless
public class NoDeSerieFacade {

    @PersistenceContext(unitName = "gstockr v2PU")
    private EntityManager em;

    public void create(NoDeSerie noDeSerie) {
        em.persist(noDeSerie);
    }

    public void edit(NoDeSerie noDeSerie) {
        em.merge(noDeSerie);
    }

    public void remove(NoDeSerie noDeSerie) {
        em.remove(em.merge(noDeSerie));
    }

    public NoDeSerie find(Object id) {
        return em.find(NoDeSerie.class, id);
    }

    public List<NoDeSerie> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(NoDeSerie.class));
        return em.createQuery(cq).getResultList();
    }

    public List<NoDeSerie> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(NoDeSerie.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<NoDeSerie> rt = cq.from(NoDeSerie.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
