    public int undeleteArray(List<Threadsmod> updatelist) {
        Integer num = 0;
        Integer[] tids = new Integer[updatelist.size()];
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            try {
                for (int i = 0; i < updatelist.size(); i++) {
                    tids[i] = updatelist.get(i).getId().getTid();
                    session.save(updatelist.get(i));
                }
                Query query = session.createQuery("update Threads as t set t.displayorder=0,t.moderated=1 where t.tid in (:tids)");
                query.setParameterList("tids", tids);
                num = query.executeUpdate();
                query = session.createQuery("update Posts as p set p.invisible = 0 where p.tid in (:tids)");
                query.setParameterList("tids", tids);
                query.executeUpdate();
                tr.commit();
            } catch (QuerySyntaxException qu) {
                qu.printStackTrace();
            }
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }
