    public boolean deleteAccountCategory(ArrayList<AccountCategory> list) throws Exception {
        Session s = null;
        try {
            s = HibernateUtils.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            boolean result = true;
            String query = "delete from AccountCategory R where R.categoryId=? and R.uid=?";
            Query q = s.createQuery(query);
            for (AccountCategory c : list) {
                int u = updateAccountForCategory(s, c.getCategoryId(), c.getParentCategoryId());
                if (u == 0) continue;
                q.setLong(0, c.getCategoryId());
                q.setLong(1, c.getUid());
                int r = q.executeUpdate();
                if (r != 1) {
                    result = false;
                    break;
                }
            }
            if (result) s.getTransaction().commit(); else {
                s.getTransaction().rollback();
            }
            return result;
        } catch (Exception e) {
            s.getTransaction().rollback();
            throw e;
        } finally {
            if (s != null) HibernateUtils.closeSession();
        }
    }
