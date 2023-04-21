    public static int deleteBlacklist(int ownerId, String[] otherIds) {
        if (otherIds == null || otherIds.length == 0) return 0;
        StringBuffer hql = new StringBuffer("DELETE FROM MyBlackListBean f WHERE f.myId=? AND f.other.id IN (");
        for (int i = 0; i < otherIds.length; i++) {
            hql.append("?,");
        }
        hql.append("?)");
        Session ssn = getSession();
        try {
            beginTransaction();
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, ownerId);
            int i = 0;
            for (; i < otherIds.length; i++) {
                String s_id = (String) otherIds[i];
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
        }
    }
