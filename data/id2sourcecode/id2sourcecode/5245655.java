    public boolean copyTo(CSPath newPath) throws AccessControlException {
        CloudController.checkRead(this);
        CloudController.checkWrite(newPath);
        try {
            if (isDirectory()) {
                FileUtils.copyDirectoryToDirectory(getPhysicalFile(), newPath.getPhysicalFile());
            } else {
                FileUtils.copyFileToDirectory(getPhysicalFile(), newPath.getPhysicalFile());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
