    public void deleteAllMessages() {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("DELETE FROM ost_messages").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
