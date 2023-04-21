    public void deleteForumrecommend(List<Integer> tidList) {
        Transaction transaction = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            Session session = factory.getCurrentSession();
            transaction = session.beginTransaction();
            String hql = "DELETE Forumrecommend AS f WHERE f.tid=?";
            Query query = session.createQuery(hql);
            for (int i = 0; i < tidList.size(); i++) {
                query.setInteger(0, tidList.get(i));
                query.executeUpdate();
            }
            transaction.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
