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
                    StringBuilder sb = new StringBuilder("delete from ADDRESS_FILTER_ITEM where id in (");
                    for (int i = 0; i < nextGroup; i++) {
                        sb.append(ids[currentIndex + i]).append(',');
                    }
                    sb.setCharAt(sb.length() - 1, ')');
                    Query query = session.createSQLQuery(sb.toString()).setCacheable(false);
                    query.executeUpdate();
                    currentIndex += nextGroup;
                }
                tx.commit();
                session.flush();
                session.clear();
            } catch (HibernateException e) {
                log.warn("Exception in fastDeleteItems: ", e);
                tx.rollback();
            }
        }
        fireDataChange(PART.ITEMS);
    }
