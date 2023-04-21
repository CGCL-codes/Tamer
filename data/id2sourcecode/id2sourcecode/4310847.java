    @Override
    protected void setUp() throws Exception {
        if (sessionDatasource == null) {
            String baseDir = "c:\temp";
            if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
                baseDir = "/home/tim/temp/";
            }
            File file = new File(baseDir);
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("make temp dir - " + baseDir);
            }
            File realConfigFile = new File(baseDir + File.separator + "config.xml");
            if (!realConfigFile.exists()) {
                File junitConfigFile = new File("." + File.separator + "config" + File.separator + "config.xml");
                if (!junitConfigFile.exists()) {
                    throw new RuntimeException("Unable to find the JUnit config file");
                }
                FileUtils.copyFile(junitConfigFile, realConfigFile);
            }
            License.initialize(new FileReader("." + File.separator + "config" + File.separator + "license.lic"));
            if (MasterDatastoreManager.getMasterDatasource() == null) {
                masterDatasource = new MasterDatasource();
                MasterDatastoreManager.setMasterDatasource(masterDatasource);
                masterDatasource.initialise(baseDir);
            } else {
                masterDatasource = (MasterDatasource) MasterDatastoreManager.getMasterDatasource();
            }
            sessionToken = MasterDatastoreManager.createSession("test", "password");
            sessionDatasource = MasterDatastoreManager.getSessionDatasource(sessionToken);
        }
        DataSource datasource = sessionDatasource.getDataSource();
        Portfolio rootPortfolio = (Portfolio) datasource.getRoot();
        Set<Portfolio> portfolios = rootPortfolio.getPortfolios();
        for (Portfolio portfolio : portfolios) {
            portfolio.delete();
        }
        try {
            sessionDatasource.getDataSource().getVcsProvider().commit();
        } catch (VCSUpdateNeededException vcsUpdateNeededException) {
            sessionDatasource.getDataSource().getVcsProvider().update();
            sessionDatasource.getDataSource().getVcsProvider().commit();
        } catch (VCSCleanNeededException vcsCleanNeededException) {
            sessionDatasource.getDataSource().getVcsProvider().cleanup();
            sessionDatasource.getDataSource().getVcsProvider().commit();
        }
        Portfolio newPortfolio = rootPortfolio.createPortfolio("Test Portfolio");
        masterDatasource.saveAndAdd(newPortfolio);
        Organization newOrganization = newPortfolio.createOrganization("Test Organization");
        masterDatasource.saveAndAdd(newOrganization);
        testPortfolio = (TransientPortfolio) sessionDatasource.getTransientElement(newPortfolio.getUuid());
        testOrganization = (TransientOrganization) sessionDatasource.getTransientElement(newOrganization.getUuid());
        sessionDatasource.getDataSource().getVcsProvider().commit();
        super.setUp();
    }
