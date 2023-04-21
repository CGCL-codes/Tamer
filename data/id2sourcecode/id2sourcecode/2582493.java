    protected void setUp() throws Exception {
        super.setUp();
        upgradeDir.mkdirs();
        assertTrue(upgradeDir.isDirectory());
        assertTrue(upgradeDir.canWrite());
        assertTrue(XUSE_00_01_00_FILES.exists());
        assertTrue(XUSE_00_01_00_FILES.isDirectory());
        FileUtils.copyDirectory(XUSE_00_01_00_FILES, upgradeDir);
        List fileList = this.getUpgradeDirectoryListing();
        assertFalse("Expected to find some files in working directory", fileList.isEmpty());
        assertTrue(fileList.contains("src"));
        this.doTestsOnRequirementsDir(NO_UPGRADE);
        this.doTestsOnUsecaseDir(NO_UPGRADE);
        this.doTestsOnResourcesDir(NO_UPGRADE);
    }
