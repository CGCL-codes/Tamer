    public void cleanup() throws Exception {
        String deleteContactHQL = "delete from Contact";
        String deleteCustomerHQL = "delete from Customer";
        SimpleJtaTransactionManagerImpl.getInstance().begin();
        try {
            Session session = getSessions().getCurrentSession();
            session.createQuery(deleteContactHQL).setFlushMode(FlushMode.AUTO).executeUpdate();
            session.createQuery(deleteCustomerHQL).setFlushMode(FlushMode.AUTO).executeUpdate();
            SimpleJtaTransactionManagerImpl.getInstance().commit();
        } catch (Exception e) {
            SimpleJtaTransactionManagerImpl.getInstance().rollback();
            throw e;
        }
    }
