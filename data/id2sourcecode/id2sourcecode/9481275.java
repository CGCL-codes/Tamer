    public void save(boolean deleteSrc) throws IOException {
        File dest = Config.getDataFile(getInternalDate(), getPhysMessageID());
        if (deleteSrc) {
            FileUtils.moveFile(file, dest);
        } else {
            FileUtils.copyFile(file, dest);
        }
    }
