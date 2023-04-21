    public static void fileCopy(String sourceFileName, String targetFileName) {
        try {
            FileUtils.copyFile(new File(sourceFileName), mkdirs(targetFileName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
