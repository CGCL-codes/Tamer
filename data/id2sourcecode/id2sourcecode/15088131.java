    public void createEmbeddedTables() {
        em.getTransaction().begin();
        try {
            String[] tableSql = getDerbyCreate();
            for (String query : tableSql) {
                if (query.matches("^\\s*$")) continue;
                em.createNativeQuery(query).executeUpdate();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
