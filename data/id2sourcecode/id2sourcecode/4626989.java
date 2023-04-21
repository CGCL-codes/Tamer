    protected void cleanMap() {
        synchronized (session) {
            Transaction tx = session.beginTransaction();
            try {
                session.createQuery("delete LearningFilterItem where length(searchString)>" + maxWordLength).setCacheable(false).executeUpdate();
                tx.commit();
            } catch (HibernateException e) {
                log.warn("Exception in cleanMap: ", e);
                tx.rollback();
            }
            List<Long> deleteList = new LinkedList<Long>();
            tx = session.beginTransaction();
            try {
                SQLQuery query = session.createSQLQuery("select id FROM LEARNING_FILTER_ITEM");
                query.addScalar("id", Hibernate.LONG).setCacheable(false);
                List<?> list = query.list();
                if (list.size() > maxItems) {
                    double ratio = (maxItems * 0.75) / list.size();
                    for (Object object : list) {
                        Long l = (Long) object;
                        if (Math.random() > ratio) {
                            deleteList.add(l);
                        }
                    }
                }
                tx.commit();
            } catch (HibernateException e) {
                log.warn("Exception in cleanMap: ", e);
                tx.rollback();
            }
            if (deleteList.size() > 0) {
                long[] ids = new long[deleteList.size()];
                int index = 0;
                for (Long l : deleteList) {
                    ids[index] = l;
                    index++;
                }
                fastDeleteItemsByID(ids);
            }
        }
        fireDataChange(PART.ITEMS);
    }
