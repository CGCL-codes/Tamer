    private void moveToOrphansFolder(File file) {
        File dir = source.getDescriptor().getOrphanDir();
        File dest = new File(dir.getAbsolutePath() + File.separator + file.getName());
        try {
            if (!file.isDirectory()) {
                FileUtils.copyFile(file, dest);
            } else {
                move(file, dest);
                deleteSVNFolders(dest);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to copy file to orphan dir", e);
        } catch (VCSException e) {
            logger.log(Level.WARNING, "Failed move dir to orphan dir", e);
        }
    }
