    public static int deleteRegistrationByEmail(String email) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("delete from Registration where email = :email");
            query.setString("email", email);
            int rowCount = query.executeUpdate();
            tx.commit();
            return rowCount;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
    }
