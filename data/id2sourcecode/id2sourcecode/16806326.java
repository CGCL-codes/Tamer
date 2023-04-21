    public Integer updateWords(Words words) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("update Words as w set w.replacement=:replacement where w.find like :find");
            query.setString("replacement", words.getReplacement());
            query.setString("find", words.getFind());
            query.executeUpdate();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) {
                tr.rollback();
                tr = null;
            }
            he.printStackTrace();
        }
        return 1;
    }
