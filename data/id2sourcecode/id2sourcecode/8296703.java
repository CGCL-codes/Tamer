    public Integer updateMaxsizeList(List<Attachtypes> list) {
        int updateNumber = 0;
        if (list == null && list.size() <= 0) return updateNumber;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                Attachtypes attachtypes = list.get(i);
                if (attachtypes != null && attachtypes.getMaxsize() != null) {
                    Query query = session.createQuery("update Attachtypes as a set a.maxsize = :maxsize  where a.id=:id");
                    query.setInteger("maxsize", attachtypes.getMaxsize());
                    query.setShort("id", attachtypes.getId());
                    updateNumber += query.executeUpdate();
                }
            }
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
        }
        return updateNumber;
    }
