    public int execUpdate(String queryString, boolean commit) {
        int result = -1;
        Transaction tx = null;
        try {
            Session session = _factory.getCurrentSession();
            tx = getOrBeginTransaction();
            result = session.createQuery(queryString).executeUpdate();
            if (commit) tx.commit();
        } catch (JDBCConnectionException jce) {
            _log.error("Caught Exception: Couldn't connect to datasource - " + "starting with an empty dataset");
        } catch (HibernateException he) {
            _log.error("Caught Exception: Error executing query: " + queryString, he);
            if (tx != null) tx.rollback();
        }
        return result;
    }
