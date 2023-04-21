    public static int deleteBookmarks(int ownerId, String[] bookmarkIds) {
        if (bookmarkIds == null || bookmarkIds.length == 0) return 0;
        StringBuffer hql = new StringBuffer("DELETE FROM BookmarkBean AS f WHERE f.owner=? AND f.id IN (");
        for (int i = 0; i < bookmarkIds.length; i++) {
            hql.append("?,");
        }
        hql.append("?)");
        Session ssn = getSession();
        try {
            beginTransaction();
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, ownerId);
            int i = 0;
            for (; i < bookmarkIds.length; i++) {
                String s_id = (String) bookmarkIds[i];
                int id = -1;
                try {
                    id = Integer.parseInt(s_id);
                } catch (Exception e) {
                }
                q.setInteger(i + 1, id);
            }
            q.setInteger(i + 1, -1);
            int er = q.executeUpdate();
            commit();
            return er;
        } catch (HibernateException e) {
            rollback();
            throw e;
        } finally {
            hql = null;
        }
    }
