    public int deleteAccount(long uid, long accountid) throws Exception {
        Session s = null;
        try {
            s = HibernateUtils.getSessionFactory().getCurrentSession();
            String query = "delete from Account R where R.accountId=?";
            s.beginTransaction();
            int r = deleteTransactions(s, uid, accountid);
            Query q = s.createQuery(query);
            q.setLong(0, accountid);
            r = q.executeUpdate();
            s.getTransaction().commit();
            return r;
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        }
    }
