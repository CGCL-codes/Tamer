    @Override
    public boolean renameTo(String path) throws CommandAbstractException {
        if (!isExternal) {
            return super.renameTo(path);
        }
        checkIdentify();
        if (!isReady) {
            return false;
        }
        File file = getTrueFile();
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
                        try {
                            fileChannelOut.close();
                        } catch (IOException e) {
                        }
                        logger.error("Cannot write file: {}", newFile);
                        return false;
                    }
                }
                currentFile = getRelativePath(newFile);
                isExternal = false;
                isReady = true;
                return true;
            }
        }
        return false;
    }
