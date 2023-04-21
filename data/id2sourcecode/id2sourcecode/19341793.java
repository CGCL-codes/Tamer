    protected void open() throws Exception {
        String name = getSelfTestName();
        File src = getTestDataDir();
        if (!src.exists()) {
            throw new Exception("No selftest directory for " + name);
        }
        File tmpDir = new File(getTmpDir(), "selftest");
        File tmpTestDir = new File(tmpDir, name);
        File tmpJobs = new File(tmpTestDir, "jobs");
        if (tmpJobs.exists()) {
            FileUtils.deleteDir(tmpJobs);
        }
        File tmpDefProfile = new File(tmpJobs, "ready-basic");
        FileUtils.copyFiles(new File(src, "profile"), tmpDefProfile);
        startHttpServer();
        File tmpConfDir = new File(tmpTestDir, "conf");
        tmpConfDir.mkdirs();
        File srcConf = new File(src.getParentFile(), "conf");
        FileUtils.copyFiles(srcConf, tmpConfDir);
        String globalSheetText = FileUtils.readFileAsString(new File(srcConf, "global.sheet"));
        globalSheetText = changeGlobalConfig(globalSheetText);
        File sheets = new File(tmpDefProfile, "sheets");
        File globalSheet = new File(sheets, "global.sheet");
        FileWriter fw = new FileWriter(globalSheet);
        fw.write(globalSheetText);
        fw.close();
        startHeritrix(tmpTestDir.getAbsolutePath());
        this.waitForCrawlFinish();
    }
