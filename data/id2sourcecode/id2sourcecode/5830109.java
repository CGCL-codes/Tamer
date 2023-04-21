    private void clearResults() {
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            s.beginTransaction();
            s.getNamedQuery("TestResultTruncate").executeUpdate();
            s.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.getTransaction().rollback();
        }
    }
