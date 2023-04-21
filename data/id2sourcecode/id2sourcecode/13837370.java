    public static void takeOverFile(File file) {
        if (file.exists()) {
            log.info("Found " + file.getName());
            File newFile = new CFile(file.getName());
            try {
                if (file.isDirectory()) {
                    FileUtils.copyDirectory(file, newFile);
                } else {
                    FileUtils.copyFile(file, newFile);
                }
            } catch (Exception e) {
                log.info("Unable to copy " + file.getName(), e);
            }
        }
    }
