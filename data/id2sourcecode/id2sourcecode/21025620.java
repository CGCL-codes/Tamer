    public Integer executeQuery(String query, Object... parametros) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        int num = 0, position = 1;
        try {
            tx.begin();
            Query q = em.createQuery(query);
            for (Object parametro : parametros) {
                q.setParameter(position++, parametro);
            }
            num = q.executeUpdate();
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
            return num;
        }
    }
