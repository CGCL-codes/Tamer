    private synchronized void executeUpdate(String hql) throws GridBrokerException {
        Transaction t = null;
        Session s = null;
        try {
            s = getSession();
            t = s.beginTransaction();
            s.createQuery(hql).executeUpdate();
            t.commit();
        } catch (Exception e) {
            if (t != null && t.isActive()) {
                t.rollback();
            }
            throw new GridBrokerException("Error executing query: " + hql, e);
        } finally {
            closeSession(s);
        }
    }
