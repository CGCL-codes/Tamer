    public void clearChangeLogEntry(String target, String app, ChangeLogEntry logEntry) {
        String recordId = this.gateway.parseId(logEntry.getItem().getData());
        logEntry.setRecordId(recordId);
        Session session = this.hibernateManager.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            String query = "delete ChangeLogEntry entry where entry.target=? AND entry.nodeId=? " + "AND entry.recordId=? AND entry.operation=? AND entry.app=?";
            session.createQuery(query).setString(0, target).setString(1, logEntry.getNodeId()).setString(2, logEntry.getRecordId()).setString(3, logEntry.getOperation()).setString(4, app).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            logger.error(this, e);
            if (tx != null) {
                tx.rollback();
            }
            throw new SyncException(e);
        }
    }
