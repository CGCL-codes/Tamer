    public void clearChangeLog(String target, String service, String app) {
        Session session = this.hibernateManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            String query = "delete ChangeLogEntry entry where entry.target=? AND entry.nodeId=? AND entry.app=?";
            session.createQuery(query).setString(0, target).setString(1, service).setString(2, app).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            logger.error(this, e);
            if (tx != null) {
                tx.rollback();
            }
            throw new SyncException(e);
        }
    }
