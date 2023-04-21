    @Override
    public MockData buildTestData() throws Exception {
        MockData dataDirectory = super.buildTestData();
        FileUtils.copyFileToDirectory(new File(dataDirectory.getDataDirectoryRoot().getParentFile().getParent(), "gazetteer.xml"), dataDirectory.getDataDirectoryRoot());
        FileUtils.copyFileToDirectory(new File(dataDirectory.getDataDirectoryRoot().getParentFile().getParent(), "gazetteer-synonyms.xml"), dataDirectory.getDataDirectoryRoot());
        return dataDirectory;
    }
