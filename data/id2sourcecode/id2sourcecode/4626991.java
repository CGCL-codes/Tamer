    protected void fastDeleteItemsByID(long[] ids) {
        log.debug("fastDeleteItemsByID entered");
        if (ids == null || ids.length == 0) {
            log.debug("fastDeleteItemsByID exited (list empty)");
            return;
        }
        synchronized (session) {
            log.debug("start delete " + ids.length + " items");
            Transaction tx = session.beginTransaction();
            try {
                int currentIndex = 0;
                while (currentIndex < ids.length) {
                    int nextGroup = Math.min(ids.length - currentIndex, 1000);
                    StringBuilder sb = new StringBuilder("delete from Learning_Filter_Item where id in (");
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
                log.warn("Exception in fastDeleteItems: ", e);
                tx.rollback();
            }
            log.debug("delete finished");
            maxHamOccurences = -1;
            maxSpamOccurences = -1;
            session.flush();
            session.clear();
            cache.resetCache();
        }
        fireDataChange(PART.ITEMS);
        log.debug("fastDeleteItemsByID exited");
    }
