    public void testTransform() {
        final InteractionRelationDAO iaDAO = new InteractionRelationDAO();
        Integer existingDiscussionCount = null;
        try {
            existingDiscussionCount = iaDAO.count(infoSpace, DiscussionRelation.class);
        } catch (final CannotConnectToDatabaseException e1) {
            fail("Persistence store is not available.");
        }
        if (existingDiscussionCount != null && existingDiscussionCount > 0) {
            Session s = null;
            Transaction tx = null;
            try {
                s = ModelManager.getInstance().getCurrentSession();
                tx = s.beginTransaction();
                s.createSQLQuery("DELETE infospaceitem, interactionrelation " + "USING infospaceitem LEFT JOIN interactionrelation " + "ON interactionrelation.id = infospaceitem.id " + "WHERE infospaceitem.type LIKE '%DiscussionRelation'").executeUpdate();
                s.flush();
                s.clear();
                tx.commit();
            } catch (final HibernateException he) {
                tx.rollback();
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Exception when attempting to remove all existing discussion relations - transaction rolled back!", he);
                }
                fail("Unable to remove existing DiscussionRelation entities in persistence store before running method under test.");
            } catch (final CannotConnectToDatabaseException e) {
                fail("Persistence store is not available.");
            } finally {
                s.close();
            }
        }
        try {
            new DiscussionTransformer().transform(this.infoSpace.getExtractorFQN(), this.infoSpace, new NullProgressMonitor());
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        }
        try {
            existingDiscussionCount = iaDAO.count(infoSpace, DiscussionRelation.class);
        } catch (final CannotConnectToDatabaseException e1) {
            fail("Persistence store is not available.");
        }
        if (existingDiscussionCount == null || existingDiscussionCount == 0) {
            fail("Persistence store does not contain any DiscussionRelation entities after running the method under test. This might not actually be wrong but is unexpected.");
        }
    }
