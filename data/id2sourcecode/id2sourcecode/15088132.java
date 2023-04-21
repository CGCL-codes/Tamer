    public void dropEmbeddedTables() {
        try {
            em.getTransaction().begin();
            for (String query : dropDerbyTables) em.createNativeQuery(query).executeUpdate();
            em.getTransaction().commit();
        } catch (Exception ignore) {
            em.getTransaction().rollback();
        }
    }
