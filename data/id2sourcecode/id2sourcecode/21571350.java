    public void update(UserSchedule us) throws HibernateException {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        try {
            String hql = "update UserSchedule set Status = ? where UserScheduleId = ?";
            Query query = session.createQuery(hql);
            query.setString(0, us.getStatus());
            query.setLong(1, us.getUserScheduleId());
            int rowCount = query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }
