    public void downloadComplete(TClientConnection connection) {
        String moveDir = Settings.getInstance().getMoveDir();
        String copyDir = Settings.getInstance().getCopyDir();
        String completeName = connection.getDownload().getLocalFilename();
        String fileName = FileUtils.getName(completeName);
        if (Settings.getInstance().isCopyEnabled()) {
            FileUtils.copyFile(completeName, copyDir + fileName);
        }
        if (Settings.getInstance().isMoveEnabled()) {
            FileUtils.moveFile(completeName, moveDir + fileName);
        }
        throw new java.lang.UnsupportedOperationException("Methode downloadComplete() noch nicht implementiert.");
    }
