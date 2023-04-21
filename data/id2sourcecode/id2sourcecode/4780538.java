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
