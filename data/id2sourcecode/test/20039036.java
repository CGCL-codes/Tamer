    public boolean deleteFavoritesByUid(int uid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Favorites as f where f.id.uid=?");
            query.setParameter(0, uid);
            query.executeUpdate();
            tr.commit();
            return true;
        } catch (HibernateException er) {
            if (tr != null) {
                tr.rollback();
            }
            er.printStackTrace();
        }
        return false;
    }
