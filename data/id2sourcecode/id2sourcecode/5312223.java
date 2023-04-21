    public void save(C object) {
        try {
            hSession.save(object);
        } catch (HibernateException e) {
            hTransaction.rollback();
            log.error(e.getMessage());
            log.error("Be sure your Database is in read-write mode!");
            throw e;
        }
    }
