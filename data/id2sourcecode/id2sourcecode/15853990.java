    public static int setNodeConnectorPriority(String nodeName, String connector, String priority) throws Exception {
        log.debug("Adding Node Connector: " + nodeName);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        int updateNumber = session.createQuery("update nodeconnector as nc " + "set nc.priority = :priority " + " where nc.node = :node " + "and nc.Connector = :connector ").setString("priority", priority).setString("node", nodeName).setString("connector", connector).executeUpdate();
        try {
            tx.commit();
        } catch (Exception e) {
            log.debug("Rolled Back Transaction: " + e);
            tx.rollback();
            throw new Exception("Error Adding Node \"" + nodeName + "\": " + e.getCause().getMessage());
        }
        return updateNumber;
    }
