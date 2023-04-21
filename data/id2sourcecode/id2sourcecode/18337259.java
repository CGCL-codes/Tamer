    public Integer updateSmiliesDisplayorderCode(List<Smilies> list) {
        Integer num = -1;
        String hql = "update Smilies as s set s.displayorder = :displayorder,s.code=:code where s.id=:id";
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            for (int i = 0; i < list.size(); i++) {
                query.setShort("displayorder", list.get(i).getDisplayorder());
                query.setString("code", list.get(i).getCode());
                query.setShort("id", list.get(i).getId());
                num += query.executeUpdate();
            }
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }
