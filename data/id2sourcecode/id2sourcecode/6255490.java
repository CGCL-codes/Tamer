    private void store(File file, File dest) throws IOException {
        try {
            UploadUtils.checkDirAndCreate(dest.getParentFile());
            FileUtils.copyFile(file, dest);
        } catch (IOException e) {
            log.error("Transfer file error when upload file", e);
            throw e;
        }
    }
