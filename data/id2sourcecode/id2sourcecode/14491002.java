    public int updateHQL(String hql, List conditionsList) throws HibernateException {
        if (log.isDebugEnabled()) log.debug("Hibernate update query: " + hql);
        if (log.isDebugEnabled()) log.debug("with conditions: " + conditionsList);
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        try {
            Query updateQuery = session.createQuery(hql);
            if (conditionsList != null) {
                for (Iterator it = conditionsList.iterator(); it.hasNext(); ) {
                    HQLCondition cond = (HQLCondition) it.next();
                    if (cond.getOperation().equals(HQLCondition.LIKE)) {
                        updateQuery.setParameter(cond.getField(), "%" + cond.getValue() + "%");
                    } else {
                        updateQuery.setParameter(cond.getField(), cond.getValue());
                    }
                }
            }
            int num = updateQuery.executeUpdate();
            tx.commit();
            return num;
        } catch (RuntimeException e) {
            tx.rollback();
            throw new HibernateException(e);
        }
    }
