    @Override
    public int executeNativeCommand(String sql, Object[] parameters) throws GenericPersistenceException {
        EntityManager em = null;
        try {
            em = EMFactory.getEntityManagerFactory(this.puName).createEntityManager();
            em.getTransaction().begin();
            Query query = em.createNativeQuery(sql);
            if (parameters != null) for (int count = 0; count < parameters.length; count++) query.setParameter((count + 1), parameters[count]);
            int inserted = query.executeUpdate();
            em.getTransaction().commit();
            return inserted;
        } catch (Exception ex) {
            if (em != null) if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw (GenericPersistenceException) this.createGenericPersistenceException(ex).initCause(ex);
        }
    }
