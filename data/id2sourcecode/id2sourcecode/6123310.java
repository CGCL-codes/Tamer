    protected void prepare() throws IOException {
        log.trace("prepare()");
        String baseName = FilenameUtils.getName(filePath);
        runtimeFolder = baseName + "/" + Utils.getDateAsString(new Date(), settings.getDateFormat());
        runtimeOutputFolder = outputFolder + "/" + runtimeFolder;
        String runtimeInputFolder = runtimeOutputFolder + "/input";
        File runtimeInputDir = new File(runtimeInputFolder);
        FileUtils.forceMkdir(runtimeInputDir);
        FileUtils.copyFile(new File(filePath), new File(runtimeInputFolder + "/" + baseName));
    }
