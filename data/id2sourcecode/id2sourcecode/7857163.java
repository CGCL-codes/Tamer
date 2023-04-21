    public void duplicate(File[] files, File destDir) {
        for (File file : files) {
            File duplicateFile = getDuplicateFile(file, destDir);
            if (file.isDirectory()) {
                try {
                    log.debug("duplicate directory : " + file.getPath() + " -> " + duplicateFile.getPath());
                    FileUtils.copyDirectory(file, duplicateFile);
                } catch (IOException e) {
                    log.error("Error duplicating directory", e);
                }
            } else {
                try {
                    log.debug("duplicate file : " + file.getPath() + " -> " + duplicateFile.getPath());
                    FileUtils.copyFile(file, duplicateFile);
                } catch (IOException e) {
                    log.error("Error duplicating file", e);
                }
            }
        }
    }
