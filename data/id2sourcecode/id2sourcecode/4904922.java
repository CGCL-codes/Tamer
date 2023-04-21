    public static void deleteMusic(MusicBean mbean) {
        Session ssn = getSession();
        try {
            beginTransaction();
            if (mbean.getStatus() == MusicBean.STATUS_NORMAL) {
                if (mbean.getMusicBox() != null) mbean.getMusicBox().incMusicCount(-1);
            }
            String hql = "UPDATE DiaryBean d SET d.bgSound = ? WHERE d.bgSound.id=?";
            executeUpdate(hql, new Object[] { null, new Integer(mbean.getId()) });
            ssn.delete(mbean);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
