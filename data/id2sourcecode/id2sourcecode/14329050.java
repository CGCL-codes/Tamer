    public int updateContacts(String name, String newTLF) throws Exception {
        String updateHQL = "update Contact set tlf = :cNewTLF where name = :cName";
        SimpleJtaTransactionManagerImpl.getInstance().begin();
        try {
            Session session = getSessions().getCurrentSession();
            int rowsAffected = session.createQuery(updateHQL).setFlushMode(FlushMode.AUTO).setParameter("cNewTLF", newTLF).setParameter("cName", name).executeUpdate();
            SimpleJtaTransactionManagerImpl.getInstance().commit();
            return rowsAffected;
        } catch (Exception e) {
            SimpleJtaTransactionManagerImpl.getInstance().rollback();
            throw e;
        }
    }
