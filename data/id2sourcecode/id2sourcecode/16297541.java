    public boolean delAllOlList() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction ts = null;
        try {
            ts = session.beginTransaction();
            session.createQuery("delete Onlinelist").executeUpdate();
            ts.commit();
            return true;
        } catch (HibernateException e) {
            if (ts != null) {
                ts.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
