    public static int deleteMsgs(int ownerId, String[] msgIds) {
        if (msgIds == null || msgIds.length == 0) return 0;
        int max_msg_count = Math.min(msgIds.length, 50);
        StringBuffer hql = new StringBuffer("DELETE FROM MessageBean AS f WHERE f.toUser.id=? AND f.id IN (");
        for (int i = 0; i < max_msg_count; i++) {
            hql.append("?,");
        }
        hql.append("?)");
        Session ssn = getSession();
        try {
            beginTransaction();
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, ownerId);
            int i = 0;
            for (; i < max_msg_count; i++) {
                String s_id = (String) msgIds[i];
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
