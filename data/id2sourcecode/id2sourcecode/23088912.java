    public void cleanupVariableSnapshotLast(String host) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query q = session.createQuery("delete from RlxVariableSnapshotLast vsl where vsl.variableId in (select distinct v.id from RlxVariable v where v.host = ?)");
            q.setString(0, host);
            q.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
