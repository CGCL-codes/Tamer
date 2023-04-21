    public static int executeUpdateQuery(Query q, Session em) throws Exception {
        Transaction t = null;
        int ret = -1;
        try {
            t = em.beginTransaction();
            ret = q.executeUpdate();
            t.commit();
            return ret;
        } catch (Exception e) {
            try {
                t.rollback();
            } catch (Exception ee) {
            }
            throw e;
        }
    }
