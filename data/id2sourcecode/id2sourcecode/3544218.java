    public void executeUpdate() {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            UserGroup group = (UserGroup) session.load(UserGroup.class, (Serializable) parameters.get(DataConnector.RECORD_KEY_PARAMETER));
            group.update((UserGroup) parameters.get(DataConnector.RECORD_PARAMETER));
            responseCode = 0;
            responseString = "Execution complete";
            tx.commit();
        } catch (Throwable t) {
            responseCode = 10;
            responseString = t.toString();
            t.printStackTrace();
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        }
    }
