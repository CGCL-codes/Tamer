    protected void setUp() throws Exception {
        super.setUp();
        String tempDirName = System.getProperty("java.io.tmpdir");
        File tempDir = new File(tempDirName);
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            fail("Cannot get temporary directory to run tests");
        }
        insightTestDir = tempDirName + "/insight-test-" + System.currentTimeMillis();
        File insightTestDirFile = new File(insightTestDir);
        if (!insightTestDirFile.mkdir()) {
            fail("Cannot create temporary directory to run tests");
        }
        insightTestDirFile = new File(insightTestDirFile.getAbsolutePath() + "/config");
        if (!insightTestDirFile.mkdir()) {
            fail("Cannot create temporary directory to run tests");
        }
        InputStream rawStream = getClass().getResourceAsStream("/insight-preferences.xml");
        FileOutputStream foStream = new FileOutputStream(insightTestDirFile.getAbsoluteFile() + "/insight-preferences.xml");
        byte[] readBuf = new byte[1024];
        int readCount = 0;
        while ((readCount = rawStream.read(readBuf)) != -1) {
            foStream.write(readBuf, 0, readCount);
        }
        if (null != foStream) {
            try {
                foStream.close();
            } catch (IOException e) {
            }
        }
        String insightHome = System.getProperty(InsightConstants.INSIGHT_HOME);
        if (null == insightHome || insightHome.trim().length() == 0) {
            System.setProperty(InsightConstants.INSIGHT_HOME, insightTestDir);
            haveSetInsightHome = true;
        }
    }
