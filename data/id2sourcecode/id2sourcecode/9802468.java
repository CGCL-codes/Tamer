    public static void executeUpdate(String s) throws Exception {
        UserTransaction tr = null;
        try {
            tr = getTransaction();
            EntityManager em = getEntityManager();
            if (tr != null) tr.begin(); else em.getTransaction().begin();
            em.createNativeQuery(s).executeUpdate();
            if (tr != null) tr.commit(); else em.getTransaction().commit();
        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            throw ex;
        }
    }
