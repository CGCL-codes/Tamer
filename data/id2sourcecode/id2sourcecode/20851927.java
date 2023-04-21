    @Override
    public int executeBulkOperation(String bulkQuery, Object[] parameters) throws GenericPersistenceException {
        EntityManager em = null;
        try {
            em = EMFactory.getEntityManagerFactory(this.puName).createEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery(bulkQuery);
            if (parameters != null) for (int count = 0; count < parameters.length; count++) query.setParameter((count + 1), parameters[count]);
            int rows = query.executeUpdate();
            em.getTransaction().commit();
            return rows;
        } catch (Exception ex) {
            if (em != null) if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw (GenericPersistenceException) this.createGenericPersistenceException(ex).initCause(ex);
        } finally {
            if (em != null) em.close();
        }
    }
