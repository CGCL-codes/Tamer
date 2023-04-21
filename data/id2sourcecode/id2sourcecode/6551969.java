    public boolean deleteSessionsBySid(String sid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Sessions as s where s.sid=?");
            query.setParameter(0, sid);
            query.executeUpdate();
            tr.commit();
            return true;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
