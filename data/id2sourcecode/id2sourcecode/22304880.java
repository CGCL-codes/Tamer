    public TimeSlot delete(Integer idModel) throws TechnicalException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getCurrentSession();
            transaction = session.beginTransaction();
            String hql = "delete from RecurrenceRule where elementId = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id", idModel.intValue());
            query.executeUpdate();
            hql = "delete from TimeSlot where id = :id";
            query = session.createQuery(hql);
            query.setInteger("id", idModel.intValue());
            query.executeUpdate();
            transaction.commit();
            return null;
        } catch (HibernateException ex) {
            if (transaction != null) transaction.rollback();
            throw new TechnicalException(ex);
        }
    }
