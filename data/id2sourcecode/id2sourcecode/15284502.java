    public boolean changePassword(String username, String password) {
        boolean flag = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = " update Users set password='" + password + "' where username='" + username + "' ";
            Query query = session.createQuery(hql);
            int rowcount = query.executeUpdate();
            transaction.commit();
            session.close();
            flag = true;
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return flag;
    }
