    public void setReplicated(String jobid) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query update = session.createQuery("update Task task " + "set task.replicated = :replicated" + " where task.jobid = :jobid");
            update.setParameter("replicated", "yes");
            update.setParameter("jobid", jobid);
            update.executeUpdate();
            session.flush();
            session.clear();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
