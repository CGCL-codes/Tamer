    public void moveToFailedFolder() throws Exception {
        File failedDir = new File(configurationManager.getFailedDirectory());
        File messageFile = getMessageLocation();
        if (!messageFile.renameTo(new File(failedDir, messageFile.getName()))) {
            try {
                FileUtils.copyFile(messageFile, new File(failedDir, messageFile.getName()));
                messageFile.delete();
            } catch (Exception e) {
                throw new Exception("moveToFailedFolder failed for message " + messageFile.getPath());
            }
        }
    }
