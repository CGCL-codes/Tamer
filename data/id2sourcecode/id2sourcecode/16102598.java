    public void copyToDirectory(File srcFile, File destDir) {
        if (!srcFile.exists()) {
            throw new FileSystemException("filemanager.msg.dir_doesn_exist", "Directory doesn't exists.");
        }
        if (!srcFile.canRead()) {
            throw new FileSystemException("filemanager.msg.unable_to_copy", "Unable to copy file.");
        }
        try {
            File destFile = new File(destDir.getAbsolutePath(), srcFile.getName());
            String absolutePath = JefbUtils.extractPath(srcFile);
            if (absolutePath.equals(destDir.getAbsolutePath())) {
                FileUtils.copyFile(srcFile, createNonExistentDestinationFile(destFile));
            } else {
                if (destFile.exists()) {
                    destFile.delete();
                }
                if (srcFile.isFile()) {
                    FileUtils.copyFileToDirectory(srcFile, destDir);
                } else {
                    FileUtils.copyDirectoryToDirectory(srcFile, destDir);
                }
            }
        } catch (IOException e) {
            throw new FileSystemException("filemanager.msg.unable_to_copy", "Unable to copy file. " + e.getMessage());
        }
    }
