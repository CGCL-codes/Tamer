    protected String generateWorkDirPath(String functionName, String contextPath) {
        String genDir = MessageDigestUtil.digest(System.getProperty("java.class.path"));
        String workRoot = config.getConfigString(SDLoader.KEY_SDLOADER_WORK_DIR);
        workRoot = PathUtil.replaceFileSeparator(workRoot);
        String workDirPath = PathUtil.jointPathWithSlash(workRoot, genDir);
        workDirPath = PathUtil.jointPathWithSlash(workDirPath, functionName);
        workDirPath = PathUtil.jointPathWithSlash(workDirPath, contextPath);
        return workDirPath;
    }
