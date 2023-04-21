    protected void executeUpdate(final String query) {
        try {
            entityManagerProxy.getTransaction().begin();
            entityManagerProxy.createNativeQuery(query).executeUpdate();
            entityManagerProxy.getTransaction().commit();
        } catch (final Exception e) {
            LOG.debug("Error executing update " + query, e);
            entityManagerProxy.getTransaction().rollback();
        }
    }
