    public void executeSQL(final String sql) {
        final Session session = getSession();
        final Transaction trans = session.beginTransaction();
        try {
            session.createSQLQuery(sql).executeUpdate();
            trans.commit();
        } catch (final Throwable t) {
            trans.rollback();
        }
    }
