    public boolean removeForum(Forums forum) {
        boolean flag = false;
        if (forum != null) {
            Session session = null;
            Transaction tran = null;
            Query query = null;
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                tran = session.beginTransaction();
                query = session.createQuery("delete from Forumfields as f where f.fid=" + forum.getFid());
                query.executeUpdate();
                session.delete(forum);
                flag = true;
                tran.commit();
            } catch (HibernateException e) {
                flag = false;
                if (tran != null) {
                    tran.rollback();
                }
                e.printStackTrace();
            }
        }
        return flag;
    }
