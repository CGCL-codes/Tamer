    public int update(String jpql, Map parameters) throws BadUpdateException, NotFoundException {
        int rpta = 0;
        getEntityManager().setFlushMode(FlushModeType.COMMIT);
        EntityTransaction tx = getEntityManager().getTransaction();
        try {
            tx.begin();
            Query consulta = jpql.contains(" ") ? createQuery(jpql, parameters) : createNamedQuery(jpql, parameters);
            rpta = consulta.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            PersistenceException t = ExceptionManager.getJpaThrowable(JpaOperation.EXECUTE_UPDATE, ex, getEntityManager(), null);
            throw new BadUpdateException(t.getMessage(), t.getCause());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return rpta;
    }
