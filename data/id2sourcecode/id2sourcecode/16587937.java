    public void remover(Integer id, Class classe) {
        EntityManager em = EMUtil.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createQuery("delete " + classe.getSimpleName() + " where id = :id");
            q.setParameter("id", id);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
