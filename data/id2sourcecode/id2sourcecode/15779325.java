    public int deleteArray(String[] ids) {
        StringBuffer querystr = new StringBuffer("delete from Bbcodes as b ");
        int num = -1;
        querystr.append(" where b.id in (");
        for (int i = 0; i < ids.length; i++) {
            querystr.append(ids[i].toString());
            querystr.append(",");
        }
        String str = querystr.substring(0, querystr.length() - 1);
        str = str + ")";
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery(str);
            num = query.executeUpdate();
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
        }
        return num;
    }
