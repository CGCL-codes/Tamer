    public boolean deleteUserGroup(Short groupId) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Usergroups where groupid = ?");
            query.setShort(0, groupId);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return true;
    }
