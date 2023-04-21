    public void deleteProjectCustomer(Integer projectId, String[] customerIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < customerIds.length; i++) {
                delStr = "delete from " + DoProjectCustomer.class.getName() + " where doProject.id = '" + projectId + "' and doCustomer.id = '" + customerIds[i] + "'";
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
