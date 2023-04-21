    public void executeUpdate() {
        String userId = "";
        Object idObject = parameters.get(DataConnector.RECORD_KEY_PARAMETER);
        if (idObject != null) {
            if (StringUtils.isString(idObject)) {
                String originalId = (String) idObject;
                userId = originalId.toLowerCase();
            }
        }
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            BasicUser user = (BasicUser) session.load(BasicUser.class, userId);
            user.update((BasicUser) parameters.get(DataConnector.RECORD_PARAMETER));
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
