    protected void performOkInternal(final IProject project, final AppServerPropertyPage _page) {
        JBossPropertyPage page = (JBossPropertyPage) _page;
        IFile file = project.getFile(new Path("src/main/config/jboss/oracle-ds.xml"));
        ContentHandlerTemplate.handle(file, new ContentHandlerCallback() {

            public void processHandle(Document doc) {
                Element root = doc.getDocumentElement();
                Element localDatasource = XMLUtils.getChildElementByTagName(root, "local-tx-datasource");
                Element connectionUrl = XMLUtils.getChildElementByTagName(localDatasource, "connection-url");
                connectionUrl.setTextContent(JBossOracleTabControl.this.getOracleURL());
                Element username = XMLUtils.getChildElementByTagName(localDatasource, "user-name");
                username.setTextContent(JBossOracleTabControl.this.oracle_username);
                Element password = XMLUtils.getChildElementByTagName(localDatasource, "password");
                password.setTextContent(JBossOracleTabControl.this.oracle_password);
            }
        });
        if (isAutoDeploy()) {
            final StringBuffer sb = new StringBuffer("server");
            sb.append(File.separator).append(page.getServer());
            File serverHome = new File(page.getServerHome(), sb.toString());
            if (!serverHome.exists()) {
                Logger.log(Logger.ERROR, "No JBoss server directory available in your Filesystem :: " + serverHome);
                MessageDialog.openError(_page.getShell(), "JBoss NexOpen Plugin", "No JBoss server directory available in your Filesystem :: " + serverHome);
                return;
            }
            final File deployHome = new File(serverHome, "deploy");
            File srcFile = new File(project.getLocationURI().getPath(), "src/main/config/jboss/oracle-ds.xml");
            StringBuffer ds = new StringBuffer(project.getName());
            ds.append("-").append("oracle-ds.xml");
            File oracleds = new File(deployHome, ds.toString());
            try {
                FileUtils.copyFile(srcFile, oracleds);
            } catch (IOException e) {
                Logger.log(Logger.ERROR, "Could not copy the oracle datasource file to JBoss", e);
            }
            final File lib = new File(serverHome, "lib");
            final File oracleLibrary = new File(lib, "ojdbc14.jar");
            if (!oracleLibrary.exists()) {
                InputStream driver = null;
                FileOutputStream fos = null;
                try {
                    driver = AppServerPropertyPage.toInputStream(new Path("jdbc/ojdbc14.jar"));
                    fos = new FileOutputStream(oracleLibrary);
                    IOUtils.copy(driver, fos);
                } catch (IOException e) {
                    Logger.log(Logger.ERROR, "Could not copy the Oracle driver jar file to JBoss 4.0.x", e);
                } finally {
                    try {
                        if (driver != null) {
                            driver.close();
                            driver = null;
                        }
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                            fos = null;
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
