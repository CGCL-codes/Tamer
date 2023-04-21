    public boolean updateThread(Short sourcefid, Short targetfid) {
        boolean flag = false;
        Session session = null;
        Transaction tran = null;
        Query query = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tran = session.beginTransaction();
            query = session.createQuery("update Threads as t set t.fid=" + targetfid + " where t.fid =" + sourcefid);
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
