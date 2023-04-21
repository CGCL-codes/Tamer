    public static void deleteMusics(int siteid, int[] ids) {
        Session ssn = getSession();
        try {
            StringBuffer hql = new StringBuffer("FROM MusicBean m WHERE m.site.id=? AND m.id IN (");
            StringBuffer hql2 = new StringBuffer("UPDATE DiaryBean d SET d.bgSound=? WHERE d.bgSound.id IN (");
            int i = 0;
            for (; i < ids.length; i++) {
                hql.append("?,");
                hql2.append("?,");
            }
            hql.append("?)");
            hql2.append("?)");
            Query q = ssn.createQuery(hql.toString());
            q.setInteger(0, siteid);
            i = 0;
            for (; i < ids.length; i++) {
                q.setInteger(i + 1, ids[i]);
            }
            q.setInteger(i + 1, ids[0]);
            List musics = q.list();
            if (musics.size() > 0) {
                beginTransaction();
                Query q2 = ssn.createQuery(hql2.toString());
                i = 0;
                q2.setParameter(0, null);
                for (; i < ids.length; i++) {
                    q2.setInteger(i + 1, ids[i]);
                }
                q2.setInteger(i + 1, ids[0]);
                q2.executeUpdate();
                for (i = 0; i < musics.size(); i++) {
                    MusicBean mbean = (MusicBean) musics.get(i);
                    if (mbean.getMusicBox() != null) mbean.getMusicBox().incMusicCount(-1);
                    ssn.delete(mbean);
                }
                commit();
            }
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
