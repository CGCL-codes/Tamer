    public void testExtractFromArticleSelection() {
        fail("Test needs to be customized before being run. See method comment for details.");
        CorePlugin.getDefault().getPreferenceStore().setValue("isArticleSelectionExtract", true);
        CorePlugin.getDefault().getPreferenceStore().setValue("ArticleSelectionFileName", this.path);
        final IProgressMonitor monitor = new NullProgressMonitor();
        try {
            this.ae.extract(this.infoSpace, monitor);
        } catch (final Exception e) {
            if (e instanceof MalformedURLException) {
                fail("Wiki URL is incorrect");
            }
            if (e instanceof CannotConnectToDatabaseException) {
                fail("Persistence store is not available.");
            }
            e.printStackTrace();
            fail("Exception of type " + e.getClass().getSimpleName() + " occurred.");
        }
        Session s = null;
        Transaction tx = null;
        try {
            s = ModelManager.getInstance().getCurrentSession();
            tx = s.beginTransaction();
            s.createSQLQuery("TRUNCATE actorcontentelementrelation;").executeUpdate();
            s.createSQLQuery("TRUNCATE contextrelation;").executeUpdate();
            s.createSQLQuery("TRUNCATE interactionrelation;").executeUpdate();
            s.createSQLQuery("TRUNCATE actor;").executeUpdate();
            s.createSQLQuery("TRUNCATE contentelement;").executeUpdate();
            s.createSQLQuery("TRUNCATE infospaceitemproperty;").executeUpdate();
            s.createSQLQuery("TRUNCATE infospaceitem;").executeUpdate();
            s.flush();
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception occurred when trying to delete test InfoSpace - transaction was rolled back. InfoSpace and possibly several contained entities were not deleted. Must be deleted by hand.", he);
            }
            throw he;
        } catch (final CannotConnectToDatabaseException e) {
            fail("Persistence store is not available.");
        } finally {
            s.close();
        }
    }
