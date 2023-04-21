    public static int removeTransformTag(String transformation, String tag, String newvalue) {
        log.debug("Removing Transformation Tag");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        int deletedEntries = session.createQuery("delete transformation as t " + "WHERE t.transformation = :transformation " + "and t.tag like :tag " + "and t.newvalue like :newvalue ").setString("transformation", transformation).setString("tag", tag).setString("newvalue", newvalue).executeUpdate();
        try {
            tx.commit();
        } catch (Exception e) {
            log.debug("Rolled Back Transaction: " + e);
            tx.rollback();
        }
        return deletedEntries;
    }
