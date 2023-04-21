    public int execUpdate(String queryString) {
        int result = -1;
        Transaction tx = null;
        try {
            Session session = _factory.getCurrentSession();
            tx = session.beginTransaction();
            result = session.createQuery(queryString).executeUpdate();
        } catch (JDBCConnectionException jce) {
            _log.error("Caught Exception: Couldn't connect to datasource - " + "starting with an empty dataset");
        } catch (HibernateException he) {
            _log.error("Caught Exception: Error executing query: " + queryString, he);
            if (tx != null) tx.rollback();
        }
        return result;
    }
