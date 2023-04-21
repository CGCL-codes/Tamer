    public Integer deleteSmiliesIds(List<Short> list) {
        Integer num = -1;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Smilies as s where s.id in (:ids)");
            query.setParameterList("ids", list, new ShortType());
            num = query.executeUpdate();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }
