    public boolean updatePost(Short sourcefid, Short targetfid) {
        boolean flag = false;
        Session session = null;
        Transaction tran = null;
        Query query = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery("update Posts as p set p.fid=" + targetfid + " where p.fid =" + sourcefid);
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
