    public void cleanup(Date variableCleanupDate, Date transactionCleanupDate) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String variableCleanupDateStr = sdf.format(variableCleanupDate);
            String transactionCleanupDateStr = sdf.format(transactionCleanupDate);
            Query q = session.createQuery("delete from RlxTransaction where timestamp < '" + transactionCleanupDateStr + "'");
            q.executeUpdate();
            q = session.createQuery("delete from RlxOperation where timestamp < '" + transactionCleanupDateStr + "'");
            q.executeUpdate();
            q = session.createSQLQuery("delete from transaction_param where transaction_ref_id not in (select distinct t.ref_id from transaction t)");
            q.executeUpdate();
            q = session.createSQLQuery("delete from operation_param where operation_ref_id not in (select distinct o.ref_id from operation o)");
            q.executeUpdate();
            q = session.createQuery("delete from RlxVariableSnapshot where timestamp < '" + variableCleanupDateStr + "'");
            q.executeUpdate();
            q = session.createQuery("delete from RlxVariable v where v.id not in (select distinct vs.variableId from RlxVariableSnapshot vs)");
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
