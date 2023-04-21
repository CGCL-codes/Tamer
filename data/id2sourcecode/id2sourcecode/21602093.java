    public final void update(ContactInfo info, String oldNumber) throws Exception {
        String moduleName = this.getClass().getName() + "update";
        logger.debug("begin " + moduleName);
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory;
        Session session;
        Query query;
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            query = session.getNamedQuery("org.openemergency.openPatient.database.updateContactInfo");
            query.setString("numberEmail", info.getNumberEmail());
            query.setLong("contactCodeID", info.getContactCodeID());
            query.setDate("updateDate", new Date());
            query.setString("numberEmail", oldNumber);
            query.setLong("personSerialID", info.getPersonSerialID());
            int rowCount = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.debug("ERROR " + moduleName + "  " + e);
            throw new Exception("Transaction failed (" + moduleName + ")  ", e);
        } finally {
            session.close();
        }
        sessionFactory.close();
        logger.debug("end " + moduleName);
    }
