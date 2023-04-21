    public void delete(MedicineList ml) {
        Session session = mpower_hibernate.HibernateUtil.currentSession();
        Transaction transaction = session.beginTransaction();
        try {
            org.hibernate.Query query = session.createQuery(" delete " + " from  " + " MedicineList ml WHERE ml.id = ? ");
            query.setLong(0, ml.getId());
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw e;
        } finally {
            mpower_hibernate.HibernateUtil.closeSession();
        }
    }
