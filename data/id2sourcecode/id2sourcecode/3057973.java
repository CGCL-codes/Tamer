    public static void executeUpdate(Object o, Session em) throws Exception {
        Transaction t = null;
        try {
            t = em.beginTransaction();
            em.update(o);
            t.commit();
        } catch (Exception e) {
            try {
                t.rollback();
            } catch (Exception ee) {
            }
            throw e;
        }
    }
