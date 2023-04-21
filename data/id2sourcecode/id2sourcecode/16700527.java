    public int update(String strUpdate, Map<String, Object> mapVal) throws RuntimeException {
        int rtnVal = 0;
        EntityManager em = getEntityManager();
        if (em != null) {
            EntityTransaction et = em.getTransaction();
            try {
                Query query = em.createQuery(strUpdate);
                if (mapVal != null) {
                    for (String mKey : mapVal.keySet()) {
                        Object mObj = mapVal.get(mKey);
                        query.setParameter(mKey, mObj);
                    }
                }
                if (query != null) {
                    if (et.isActive() == false) {
                        et.begin();
                    }
                    rtnVal = query.executeUpdate();
                    if (et.isActive()) {
                        et.commit();
                    }
                }
            } catch (Exception exception) {
                if (et.isActive()) {
                    et.rollback();
                }
                em.close();
                throw new RuntimeException(exception);
            }
            em.close();
        }
        return rtnVal;
    }
