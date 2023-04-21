    private void execute(String folderSrc, String folderDest) {
        try {
            File folderSrcFile = new File(folderSrc);
            File[] originalFilesSrc = folderSrcFile.listFiles();
            File folderDestFile = new File(folderDest);
            File[] originalFilesDest = folderDestFile.listFiles();
            if (!folderDestFile.exists()) {
                folderDestFile.mkdirs();
                logger.debug("creating " + folderDestFile.getAbsolutePath());
            }
            for (int i = 0; i < originalFilesSrc.length; i++) {
                if (originalFilesSrc[i].isDirectory()) {
                    String newFolder = folderDestFile.getAbsolutePath() + File.separatorChar + originalFilesSrc[i].getName();
                    File toCreateFolder = new File(newFolder);
                    execute(originalFilesSrc[i].getAbsolutePath(), toCreateFolder.getAbsolutePath());
                } else {
                    File newFile = new File(folderDest + File.separatorChar + originalFilesSrc[i].getName());
                    if (!newFile.exists()) {
                        copyFile(originalFilesSrc[i], newFile);
                    } else {
                        if (newFile.lastModified() != originalFilesSrc[i].lastModified()) {
                            newFile.delete();
                            logger.info("deleting " + newFile.getAbsolutePath());
                            copyFile(originalFilesSrc[i], newFile);
                        }
                    }
                }
            }
            if (deleteFilesNotInSource && null != originalFilesDest) {
                List<Integer> toKeep = new ArrayList<Integer>();
                for (int i = 0; i < originalFilesDest.length; i++) {
                    File dest = originalFilesDest[i];
                    for (int j = 0; j < originalFilesSrc.length; j++) {
                        File src = originalFilesSrc[j];
                        if (src.getName().equals(dest.getName())) {
                            toKeep.add(i);
                        }
                    }
                }
                for (int i = 0; i < originalFilesDest.length; i++) {
                    if (!toKeep.contains(i)) {
                        if (originalFilesDest[i].isDirectory()) {
                            FileUtil.deleteFolder(originalFilesDest[i]);
                        } else {
                            originalFilesDest[i].delete();
                        }
                        logger.info("deleting " + originalFilesDest[i].getAbsolutePath());
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
