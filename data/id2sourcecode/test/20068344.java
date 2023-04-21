    public void delete(String className, int id) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String deleteString = "delete from " + className + " where id = ?";
            Query queryObject = getSession().createQuery(deleteString);
            queryObject.setParameter(0, id);
            queryObject.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
