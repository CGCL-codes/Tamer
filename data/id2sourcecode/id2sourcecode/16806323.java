    public Integer delteCollection(String[] ids) {
        int count = -1;
        StringBuffer deleteString = new StringBuffer("delete from Words as w");
        if (ids != null && ids.length > 0) {
            deleteString.append(" where w.id in (");
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null) {
                    deleteString.append(ids[i]);
                    deleteString.append(",");
                }
            }
            String querystr = deleteString.substring(0, deleteString.length() - 1);
            querystr = querystr + ")";
            Transaction tr = null;
            try {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tr = session.beginTransaction();
                Query query = session.createQuery(querystr);
                count = query.executeUpdate();
                session.flush();
            } catch (HibernateException he) {
                if (tr != null) tr.rollback();
                tr = null;
                he.printStackTrace();
            }
            if (tr != null) tr.commit();
        } else {
            return count;
        }
        return count;
    }
