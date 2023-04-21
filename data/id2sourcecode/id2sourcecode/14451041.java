    public Integer deleteArray(String[] aids) {
        Integer deleteNumber = -1;
        Transaction tr = null;
        try {
            Integer[] ids = new Integer[aids.length];
            for (int i = 0; i < aids.length; i++) {
                ids[i] = Integer.valueOf(aids[i]);
            }
            String queryStr = "delete from Attachments as a where a.aid in (:ids)";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery(queryStr);
            query.setParameterList("ids", ids, new org.hibernate.type.IntegerType());
            deleteNumber = query.executeUpdate();
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            if (tr != null) tr.rollback();
            tr = null;
        }
        return deleteNumber;
    }
