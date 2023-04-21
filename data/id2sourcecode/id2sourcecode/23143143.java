    protected void fastDeleteItemsByID(long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        synchronized (session) {
            Transaction tx = session.beginTransaction();
            try {
                int currentIndex = 0;
                while (currentIndex < ids.length) {
                    int nextGroup = Math.min(ids.length - currentIndex, 1000);
                    StringBuilder sb = new StringBuilder("delete from Links_Filter_Item where id in (");
                    for (int i = 0; i < nextGroup; i++) {
                        sb.append(ids[currentIndex + i]).append(',');
                    }
                    sb.setCharAt(sb.length() - 1, ')');
                    Query query = session.createSQLQuery(sb.toString()).setCacheable(false);
                    query.executeUpdate();
                    currentIndex += nextGroup;
                }
                tx.commit();
            } catch (HibernateException e) {
                log.warn("Exception in fastDeleteItemsByID: ", e);
                tx.rollback();
            }
            session.flush();
            session.clear();
        }
        fireDataChange(PART.ITEMS);
    }
