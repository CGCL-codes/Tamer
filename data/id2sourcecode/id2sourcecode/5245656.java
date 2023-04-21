    public boolean moveTo(CSPath newPath) throws AccessControlException {
        CloudController.checkWrite(this);
        CloudController.checkWrite(newPath);
        try {
            if (isDirectory()) {
                FileUtils.copyDirectoryToDirectory(getPhysicalFile(), newPath.getPhysicalFile());
                FileUtils.deleteDirectory(getPhysicalFile());
            } else {
                FileUtils.copyFileToDirectory(getPhysicalFile(), newPath.getPhysicalFile());
                FileUtils.forceDelete(getPhysicalFile());
            }
            return true;
        } catch (IOException e) {
            logger.error("Could not move file", e);
            return false;
        }
    }
