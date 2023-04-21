    public static int incViewCount(int incCount, int[] music_ids) {
        StringBuffer hql = new StringBuffer("UPDATE MusicBean d SET d.viewCount=d.viewCount+? WHERE d.id IN (");
        for (int i = 0; i < music_ids.length; i++) {
            if (i > 0) hql.append(',');
            hql.append('?');
        }
        hql.append(')');
        try {
            Session ssn = getSession();
            beginTransaction();
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, incCount);
            for (int i = 1; i <= music_ids.length; i++) {
                q.setParameter(i, new Integer(music_ids[i - 1]));
            }
            int er = q.executeUpdate();
            commit();
            return er;
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
