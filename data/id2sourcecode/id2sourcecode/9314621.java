    protected void performOkInternal(IProject project, AppServerPropertyPage _page) {
        JBossPropertyPage page = (JBossPropertyPage) _page;
        IFile file = project.getFile(new Path("src/main/config/jboss/mysql-ds.xml"));
        ContentHandlerTemplate.handle(file, new ContentHandlerCallback() {

            public void processHandle(Document doc) {
                Element root = doc.getDocumentElement();
                Element localDatasource = XMLUtils.getChildElementByTagName(root, "local-tx-datasource");
                Element connectionUrl = XMLUtils.getChildElementByTagName(localDatasource, "connection-url");
                connectionUrl.setTextContent(JBossMySQLTabControl.this.getMySQLURL());
                Element username = XMLUtils.getChildElementByTagName(localDatasource, "user-name");
                username.setTextContent(JBossMySQLTabControl.this.mysql_username);
                Element password = XMLUtils.getChildElementByTagName(localDatasource, "password");
                password.setTextContent(JBossMySQLTabControl.this.mysql_password);
            }
        });
        if (isAutoDeploy()) {
            StringBuffer sb = new StringBuffer("server");
            sb.append(File.separator).append(page.getServer());
            File serverHome = new File(page.getServerHome(), sb.toString());
            if (!serverHome.exists()) {
                Logger.log(Logger.ERROR, "No JBoss server directory available in your Filesystem :: " + serverHome);
                MessageDialog.openError(_page.getShell(), "JBoss NexOpen Plugin", "No JBoss server directory available in your Filesystem :: " + serverHome);
                return;
            }
            File deployHome = new File(serverHome, "deploy");
            File srcFile = new File(project.getLocationURI().getPath(), "src/main/config/jboss/mysql-ds.xml");
            StringBuffer ds = new StringBuffer(project.getName());
            ds.append("-").append("mysql-ds.xml");
            File oracleds = new File(deployHome, ds.toString());
            try {
                FileUtils.copyFile(srcFile, oracleds);
            } catch (IOException e) {
                Logger.log(Logger.ERROR, "Could not copy the mysql datasource file to JBoss", e);
            }
            final File lib = new File(serverHome, "lib");
            final File mysqlLibrary = new File(lib, NexOpenUIActivator.getDefault().getMySQLDriver());
            if (!mysqlLibrary.exists()) {
                InputStream driver = null;
                FileOutputStream fos = null;
                try {
                    driver = AppServerPropertyPage.toInputStream(new Path("jdbc/" + NexOpenUIActivator.getDefault().getMySQLDriver()));
                    fos = new FileOutputStream(mysqlLibrary);
                    IOUtils.copy(driver, fos);
                } catch (IOException e) {
                    Logger.log(Logger.ERROR, "Could not copy the MySQL Driver jar file to JBoss 4.0.x or JBoss 4.2.x", e);
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
