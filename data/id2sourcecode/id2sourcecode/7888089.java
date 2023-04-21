    public final void batchInsert(final Collection<? extends T> objects) throws CannotConnectToDatabaseException {
        if (objects != null && !objects.isEmpty()) {
            final Session s = this.currentSession();
            Transaction tx = null;
            try {
                tx = s.beginTransaction();
                final String oldCacheMode = s.getCacheMode().toString();
                if (s.getCacheMode() != CacheMode.IGNORE) {
                    s.setCacheMode(CacheMode.IGNORE);
                }
                s.createSQLQuery("SET foreign_key_checks=0;").executeUpdate();
                int itemCount = 0;
                for (T obj : objects) {
                    s.saveOrUpdate(obj);
                    if (++itemCount % BATCH_SIZE == 0) {
                        s.flush();
                        s.clear();
                    }
                }
                s.createSQLQuery("SET foreign_key_checks=1;").executeUpdate();
                s.setCacheMode(CacheMode.parse(oldCacheMode));
                s.clear();
                tx.commit();
            } catch (HibernateException he) {
                tx.rollback();
                LOGGER.error("Failed to batch insert entities. This method contains MySQL specific code - transaction was rolled back.", he);
                throw he;
            } finally {
                s.close();
            }
        }
    }
