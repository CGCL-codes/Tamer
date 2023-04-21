    private boolean backup(File file, boolean isBackupEnable) throws IOException {
        if (!isBackupEnable || !file.exists()) {
            return true;
        }
        String path;
        try {
            path = file.getCanonicalPath();
            String backupFilePath = path.substring(0, path.indexOf(".xls")) + "_back.xls";
            File backupFile = new File(backupFilePath);
            FileUtils.copyFile(file, backupFile);
        } catch (IOException e) {
            throw new IOException(ResourceString.getResourceString("error.backup.excel.file"));
        }
        return true;
    }
