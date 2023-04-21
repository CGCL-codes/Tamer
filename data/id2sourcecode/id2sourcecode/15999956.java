    public int executeNativeUpdate(String jpql, Map parameters) throws NotFoundException {
        int rpta = 0;
        getEntityManager().setFlushMode(FlushModeType.COMMIT);
        EntityTransaction tx = getEntityManager().getTransaction();
        try {
            tx.begin();
            rpta = createNativeQuery(jpql, parameters).executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            PersistenceException t = ExceptionManager.getJpaThrowable(JpaOperation.EXECUTE_UPDATE, ex, getEntityManager(), null);
            throw new NotFoundException(t.getMessage(), t.getCause());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return rpta;
    }
