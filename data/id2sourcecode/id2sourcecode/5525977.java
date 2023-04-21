    @SuppressWarnings("unchecked")
    public final int delete(Class beanClass, int id, Session session) throws HibernateException {
        Query query = session.createQuery("delete from " + beanClass.getName() + " o where o.id =" + id);
        Transaction tx = null;
        int result = -1;
        try {
            tx = session.beginTransaction();
            result = query.executeUpdate();
            tx.commit();
            session.flush();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            session.close();
            throw e;
        }
        return result;
    }
