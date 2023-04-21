    public boolean deleteAccountCategory(AccountCategory c) throws Exception {
        Session s = null;
        try {
            s = HibernateUtils.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            boolean result = true;
            String query = "delete from AccountCategory R where R.categoryId=? and R.uid=?";
            Query q = s.createQuery(query);
            q.setLong(0, c.getCategoryId());
            q.setLong(1, c.getUid());
            int r = q.executeUpdate();
            if (r != 1) {
                s.getTransaction().rollback();
            } else {
                s.getTransaction().commit();
            }
            return result;
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        } finally {
            if (s != null) HibernateUtils.closeSession();
        }
    }
