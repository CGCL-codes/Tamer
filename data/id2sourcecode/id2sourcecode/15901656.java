    public static int deleteFriend(int ownerId, String[] friendIds) {
        if (friendIds == null || friendIds.length == 0) return 0;
        StringBuffer hql = new StringBuffer("DELETE FROM FriendBean f WHERE f.owner=? AND f.friend.id IN (");
        for (int i = 0; i < friendIds.length; i++) {
            hql.append("?,");
        }
        hql.append("?)");
        Session ssn = getSession();
        try {
            beginTransaction();
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, ownerId);
            int i = 0;
            for (; i < friendIds.length; i++) {
                String s_id = (String) friendIds[i];
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
