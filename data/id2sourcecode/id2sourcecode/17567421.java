    public void removeUserbyPrimaryKey(Integer primaryKey) throws Exception {
        Users user = null;
        Session session = null;
        Transaction tx = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            user = (Users) session.get(Users.class, primaryKey);
            if (user != null) {
                session.delete(user);
                String query = "delete from user_roles where user_name='" + user.getUser_name() + "'";
                session.createSQLQuery(query).executeUpdate();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            logger.error("EXCEPTION While removing user", e);
            session.getTransaction().rollback();
            session.close();
            throw e;
        }
    }
