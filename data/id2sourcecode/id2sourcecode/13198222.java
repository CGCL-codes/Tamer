    protected FileChannel getFileChannel() {
        if (!isReady) {
            return null;
        }
        File trueFile;
        try {
            trueFile = getFileFromPath(currentFile);
        } catch (CommandAbstractException e1) {
            return null;
        }
        FileChannel fileChannel;
        try {
            FileInputStream fileInputStream = new FileInputStream(trueFile);
            fileChannel = fileInputStream.getChannel();
            if (position != 0) {
                fileChannel = fileChannel.position(position);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found in getFileChannel:", e);
            return null;
        } catch (IOException e) {
            logger.error("Change position in getFileChannel:", e);
            return null;
        }
        return fileChannel;
    }
