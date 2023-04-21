    public Integer deleteAll() throws Exception {
        int num = 0;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Words as w");
            num = query.executeUpdate();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
                tr = null;
            }
            he.printStackTrace();
            throw he;
        }
        return num;
    }
