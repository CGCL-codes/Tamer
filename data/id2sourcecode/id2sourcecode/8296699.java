    public Integer deleteList(String[] ids) {
        int count = -1;
        StringBuffer deleteString = new StringBuffer("delete from Attachtypes as a");
        if (ids != null && ids.length > 0) {
            deleteString.append(" where a.id in (");
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
                tr.commit();
            } catch (HibernateException he) {
                if (tr != null) tr.rollback();
                tr = null;
                he.printStackTrace();
            }
        } else {
            return count;
        }
        return count;
    }
