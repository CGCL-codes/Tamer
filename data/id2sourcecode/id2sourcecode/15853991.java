    public static int removeNodeConnector(String nodeName, String connector) {
        log.debug("Removing Node Connector");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        int deletedEntries = session.createQuery("delete nodeconnector as nc " + "WHERE nc.node like :node " + "and nc.connector like :connector ").setString("node", nodeName).setString("connector", connector).executeUpdate();
        try {
            tx.commit();
        } catch (Exception e) {
            log.debug("Rolled Back Transaction: " + e);
            tx.rollback();
        }
        return deletedEntries;
    }
