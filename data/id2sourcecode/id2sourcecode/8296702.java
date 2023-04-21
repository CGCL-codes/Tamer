    public Integer updateExtensionList(List<Attachtypes> list) {
        int updateNumber = 0;
        if (list == null && list.size() <= 0) return updateNumber;
        for (int i = 0; i < list.size(); i++) {
            Attachtypes attachtypes = list.get(i);
            if (attachtypes != null && attachtypes.getExtension() != null) {
                if (isSave(attachtypes.getExtension()) == false) {
                    list.remove(i);
                }
            }
        }
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                Attachtypes attachtypes = list.get(i);
                if (attachtypes != null && attachtypes.getExtension() != null) {
                    Query query = session.createQuery("update Attachtypes as a set a.extension = :extension  where a.id=:id");
                    query.setString("extension", attachtypes.getExtension());
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
