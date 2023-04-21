    public Integer updateDisplayorderIcons(List<Smilies> list) {
        int updateNumber = 0;
        if (list == null && list.size() <= 0) return updateNumber;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                Smilies s = list.get(i);
                if (s != null) {
                    Query query = session.createQuery("update Smilies as s set s.displayorder = :displayorder  where s.id=:id");
                    query.setShort("displayorder", s.getDisplayorder());
                    query.setShort("id", s.getId());
                    updateNumber += query.executeUpdate();
                }
            }
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
        }
        return updateNumber;
    }
