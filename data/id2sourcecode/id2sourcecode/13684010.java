    public Integer deleteArray(List deletelist) {
        Integer num = 0;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            try {
                Query query = session.createQuery("delete from Threads as t where t.tid in (:tids)");
                query.setParameterList("tids", deletelist, new org.hibernate.type.IntegerType());
                num = query.executeUpdate();
                query = session.createQuery("delete from Posts as p where p.tid in (:tids)");
                query.setParameterList("tids", deletelist, new org.hibernate.type.IntegerType());
                query.executeUpdate();
                query = session.createQuery("delete from Threadsmod as m where m.id.tid in (:tids)");
                query.setParameterList("tids", deletelist, new org.hibernate.type.IntegerType());
                query.executeUpdate();
                query = session.createQuery("delete from Attachments as a where a.tid in (:tids)");
                query.setParameterList("tids", deletelist, new org.hibernate.type.IntegerType());
                query.executeUpdate();
                tr.commit();
            } catch (QuerySyntaxException qu) {
                qu.printStackTrace();
            }
        } catch (HibernateException he) {
            if (tr != null && tr.isActive()) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }
