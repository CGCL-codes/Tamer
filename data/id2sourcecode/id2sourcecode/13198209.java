    public boolean renameTo(String path) throws CommandAbstractException {
        checkIdentify();
        if (!isReady) {
            return false;
        }
        File file = getFileFromPath(currentFile);
        if (file.canRead()) {
            File newFile = getFileFromPath(path);
            if (newFile.getParentFile().canWrite()) {
                if (!file.renameTo(newFile)) {
                    FileOutputStream fileOutputStream;
                    try {
                        fileOutputStream = new FileOutputStream(newFile);
                    } catch (FileNotFoundException e) {
                        logger.warn("Cannot find file: " + newFile.getName(), e);
                        return false;
                    }
                    FileChannel fileChannelOut = fileOutputStream.getChannel();
                    if (get(fileChannelOut)) {
                        delete();
                    } else {
                        logger.warn("Cannot write file: {}", newFile);
                        return false;
                    }
                }
                currentFile = getRelativePath(newFile);
                isReady = true;
                return true;
            }
        }
        return false;
    }
