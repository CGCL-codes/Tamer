    public int execUpdate(String queryString) throws HibernateException {
        int result = -1;
        Transaction tx = null;
        try {
            Session session = _factory.getCurrentSession();
            tx = session.beginTransaction();
            result = session.createQuery(queryString).executeUpdate();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            throw he;
        }
        return result;
    }
