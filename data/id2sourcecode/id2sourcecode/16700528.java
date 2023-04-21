    public int update(String strUpdate, Object... objs) throws RuntimeException {
        int rtnVal = 0;
        EntityManager em = getEntityManager();
        if (em != null) {
            EntityTransaction et = em.getTransaction();
            try {
                Query query = em.createQuery(strUpdate);
                if (objs != null) {
                    int index = 1;
                    for (Object obj : objs) {
                        query.setParameter(index++, obj);
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
