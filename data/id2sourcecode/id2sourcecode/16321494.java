    protected static int commitUpdate(String hql, Object[] args) {
        try {
            Session ssn = getSession();
            beginTransaction();
            Query q = ssn.createQuery(hql);
            for (int i = 0; args != null && i < args.length; i++) {
                q.setParameter(i, args[i]);
            }
            int er = q.executeUpdate();
            commit();
            return er;
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
