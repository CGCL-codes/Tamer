    public void deletePSIBlastDatabase(long id) {
        Transaction transaction = null;
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        try {
            transaction = session.beginTransaction();
            Query q = session.createQuery("delete from " + DATABASE_ITEM_TABLE + " as s where s.id =:id");
            q.setLong("id", id);
            q.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
