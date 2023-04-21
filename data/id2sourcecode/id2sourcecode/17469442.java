    public int deleteAlert(long accountid) throws Exception {
        Session s = null;
        try {
            s = HibernateUtils.getSessionFactory().getCurrentSession();
            String query = "delete from Alert R where R.accountId=?";
            s.beginTransaction();
            Query q = s.createQuery(query);
            q.setLong(0, accountid);
            int r = q.executeUpdate();
            s.getTransaction().commit();
            return r;
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        }
    }
