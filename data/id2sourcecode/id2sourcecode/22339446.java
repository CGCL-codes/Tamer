    public void deleteJob(long id) {
        Transaction transaction = null;
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        try {
            transaction = session.beginTransaction();
            Query q = session.createQuery("delete from " + JOB_TABLE + " as o where o.id = :id");
            q.setLong("id", id);
            q.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
