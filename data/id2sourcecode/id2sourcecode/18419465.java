    public void clearAll() {
        Session session = this.hibernateManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("delete RecordMap as recordmap");
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new SyncException(e);
        }
    }
