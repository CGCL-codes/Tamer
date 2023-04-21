    public final int delete(List<DeletableBean> deletableList, Session session) throws HibernateException {
        Transaction tx = null;
        int i, result = 0, size = deletableList.size();
        DeletableBean deletable = null;
        try {
            tx = session.beginTransaction();
            for (i = 0; i < size; i++) {
                deletable = deletableList.get(i);
                result += session.createQuery("delete from " + deletable.getBeanClass() + " o where o.id =" + deletable.getId()).executeUpdate();
            }
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
