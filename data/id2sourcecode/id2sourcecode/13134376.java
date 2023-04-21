    protected void ensureTransactionResources() throws Exception {
        if (getUseTransaction()) {
            FileUtils.forceMkdir(new File(getTransactionDirectoryPath()));
            String originalInputRecordFilePath = getWorkingDirectory() + "/" + getInputRecordFileName();
            FileUtils.copyFile(new File(originalInputRecordFilePath), new File(getInputRecordFilePath()));
        }
    }
