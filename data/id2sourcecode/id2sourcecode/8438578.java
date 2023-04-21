    protected int delete(String queryName, String[] params, Object[] values, Type[] types) throws HibernateException {
        try {
            _session.beginTransaction();
            Query q = _session.getNamedQuery(queryName);
            for (int i = 0; i < params.length; i++) q.setParameter(params[i], values[i], types[i]);
            int deletedEntities = q.executeUpdate();
            _session.getTransaction().commit();
            return deletedEntities;
        } catch (HibernateException e) {
            _session.getTransaction().rollback();
            throw e;
        }
    }
