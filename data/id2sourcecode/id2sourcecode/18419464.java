    public void removeRecordMap(String source, String target) {
        Session session = this.hibernateManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("delete RecordMap as recordmap where recordmap.source=? and recordmap.target=?").setString(0, source).setString(1, target);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new SyncException(e);
        }
    }
