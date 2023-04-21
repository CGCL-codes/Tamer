    public int executeQuery(Query query) {
        Session session = getSession();
        int affectedRows = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            affectedRows = query.executeUpdate();
            tx.commit();
            tx = null;
            session.refresh(this);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return affectedRows;
    }
