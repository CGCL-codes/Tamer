    public void delete(String jobid) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query delete = session.createQuery("delete from Task task" + " where task.jobid = :jobid");
            delete.setParameter("jobid", jobid);
            int row = delete.executeUpdate();
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
