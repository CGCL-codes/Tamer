    public void deleteThreadsmod(Integer tid) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Threadsmod as m WHERE m.id.tid=?");
            query.setInteger(0, tid);
            query.executeUpdate();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        }
    }
