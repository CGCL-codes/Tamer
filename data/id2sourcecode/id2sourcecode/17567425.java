    public void saveUser(Users user, String role) throws Exception {
        Session session = null;
        Transaction tx = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.save(user);
            String myquery = "insert into user_roles values ('" + user.getUser_name() + "','" + role + "')";
            session.createSQLQuery(myquery).executeUpdate();
            tx.commit();
            session.close();
        } catch (Exception e) {
            logger.error("EXCEPTION While saving user", e);
            session.getTransaction().rollback();
            session.close();
            throw e;
        }
    }
