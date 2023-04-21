    public int deleteContacts() throws Exception {
        String deleteHQL = "delete Contact where customer in ";
        deleteHQL += " (select customer FROM Customer as customer ";
        deleteHQL += " where customer.name = :cName)";
        SimpleJtaTransactionManagerImpl.getInstance().begin();
        try {
            Session session = getSessions().getCurrentSession();
            int rowsAffected = session.createQuery(deleteHQL).setFlushMode(FlushMode.AUTO).setParameter("cName", "Red Hat").executeUpdate();
            SimpleJtaTransactionManagerImpl.getInstance().commit();
            return rowsAffected;
        } catch (Exception e) {
            SimpleJtaTransactionManagerImpl.getInstance().rollback();
            throw e;
        }
    }
