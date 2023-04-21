    public void delete(String className, String[] ids) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            StringBuffer deleteSB = new StringBuffer();
            deleteSB.append("delete from " + className + " where id in (");
            for (int i = 0; i < ids.length; i++) {
                deleteSB.append(ids[i]);
                if (i < ids.length - 1) {
                    deleteSB.append(", ");
                }
            }
            deleteSB.append(")");
            Query queryObject = getSession().createQuery(deleteSB.toString());
            queryObject.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
