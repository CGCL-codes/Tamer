    public void deleteAcDataCustomer(Integer customerId, String[] userIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < userIds.length; i++) {
                if (userIds[i] == null) {
                    continue;
                }
                delStr = "delete from " + AcDataCustomer.class.getName() + " where data.id = '" + customerId + "' and viewer.id = '" + userIds[i] + "'";
                Query queryObject = getSession().createQuery(delStr);
                queryObject.executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
