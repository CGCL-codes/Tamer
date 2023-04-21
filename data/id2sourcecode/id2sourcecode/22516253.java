    public File copyFile(String path, String name, String newName) throws IOException, IllegalFileAccessException {
        if (newName != null && newName.length() > 0 && !newName.equals(name)) {
            FileMetaData subDirectory = getFileMetaData(path);
            File originalFile = new File(getFile(subDirectory), name);
            File newFile = new File(getFile(subDirectory), newName);
            if (!isUnderneathRootDirectory(originalFile) || !isUnderneathRootDirectory(newFile)) {
                throw new IllegalFileAccessException();
            }
            FileUtils.copyFile(originalFile, newFile);
            return newFile;
        } else {
            return null;
        }
    }
