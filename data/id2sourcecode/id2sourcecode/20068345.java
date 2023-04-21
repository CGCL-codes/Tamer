    public void delete(String className, String[] filters) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String deleteString = "delete from " + className + getWhereStatement(filters);
            Query queryObject = getSession().createQuery(deleteString);
            queryObject.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
