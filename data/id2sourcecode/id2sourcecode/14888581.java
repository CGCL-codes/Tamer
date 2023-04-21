    public Boolean executeUpdateByHql(String hql) {
        boolean flag = false;
        Transaction tran = null;
        Query query = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery(hql);
            query.executeUpdate();
            flag = true;
            tran.commit();
        } catch (HibernateException e) {
            flag = false;
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
        }
        return flag;
    }
