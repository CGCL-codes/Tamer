    private int tagsTemplate(StringBuffer querySQL, List<String> tagsName) throws Exception {
        int num = -1;
        querySQL.append(" where t.tagname in (:tagnames)");
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery(querySQL.toString());
            query.setParameterList("tagnames", tagsName, new org.hibernate.type.StringType());
            num += query.executeUpdate();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
                tr = null;
            }
            he.printStackTrace();
        }
        return num;
    }
