    public boolean get(String path, File localFile, boolean realtime) {
        File target = new File(directory, path);
        if (!target.exists()) return false;
        try {
            FileUtils.copyFile(target, localFile);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
