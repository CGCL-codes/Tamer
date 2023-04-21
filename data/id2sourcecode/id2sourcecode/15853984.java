    public static int removeNodeValue(String nodeName, String attribute, String value) {
        log.debug("Removing Node Value");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        int deletedEntries = session.createQuery("delete nodevalue as nv " + "WHERE nv.node like :node " + "and nv.attribute like :attribute " + "and nv.value like :value ").setString("node", nodeName).setString("attribute", attribute).setString("value", value).executeUpdate();
        try {
            tx.commit();
        } catch (Exception e) {
            log.debug("Rolled Back Transaction: " + e);
            tx.rollback();
        }
        return deletedEntries;
    }
