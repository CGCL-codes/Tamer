    public void testLoadingLockedDatasource() {
        String filePath = "C:\\temp\\xprocess.lock";
        final File tempfile = new File(filePath);
        if (!tempfile.exists()) {
            BufferedWriter out;
            try {
                out = new BufferedWriter(new FileWriter(tempfile));
                out.write("");
                out.close();
            } catch (IOException e) {
                fail("Unable to create the lock file in the temp directory");
            }
        }
        Robot.syncExec(uitu.getDisplay(), null, new Runnable() {

            public void run() {
                String datasourceLocation = DatasourceUtil.getDefaultLocation();
                try {
                    FileUtils.copyFile(tempfile, new File(datasourceLocation + File.separator + "local" + File.separator + "xprocess.lock"));
                } catch (IOException e1) {
                    fail("Unable to move the lock file to the local directory - " + e1);
                }
                try {
                    LoadManager.startupLoader(datasourceLocation);
                    assertTrue(LoadManager.hasFailedToLoad());
                } catch (DatasourceLoadException e) {
                    fail("DatasourceLoadException - " + e.getMessage());
                }
            }
        });
    }
