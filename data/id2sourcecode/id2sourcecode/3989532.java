    public void deleteCustomerRelation(Integer id, String[] customerIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < customerIds.length; i++) {
                delStr = "delete from " + DoCustomerRelation.class.getName() + " where (doCustomerByDoCustomerId1.id = '" + id + "' and doCustomerByDoCustomerId2.id = '" + customerIds[i] + "') or (doCustomerByDoCustomerId2.id = '" + id + "' and doCustomerByDoCustomerId1.id = '" + customerIds[i] + "') ";
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
