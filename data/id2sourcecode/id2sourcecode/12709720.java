    public void updateForumsByHQL(String hql) {
        Transaction transaction = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
