    public void update(C object) {
        try {
            sess().update(object);
        } catch (HibernateException e) {
            hTransaction.rollback();
            log.error(e.getMessage());
            log.error("Be sure your Database is in read-write mode!");
            throw e;
        }
    }
