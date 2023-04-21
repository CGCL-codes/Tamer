    public void createStorageSchema(String type) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            String schema = "/storage-" + type + ".sql";
            session = sessionFactory.openSession();
            SQLQuery sqlQuery = session.createSQLQuery(AppUtil.readClasspathFile(schema));
            sqlQuery.executeUpdate();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
