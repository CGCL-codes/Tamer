    @Override
    protected FileChannel getFileChannel() {
        if (!isExternal) {
            return super.getFileChannel();
        }
        if (!isReady) {
            return null;
        }
        File trueFile = getTrueFile();
        FileChannel fileChannel;
        try {
            FileInputStream fileInputStream = new FileInputStream(trueFile);
            fileChannel = fileInputStream.getChannel();
            if (getPosition() > 0) {
                fileChannel = fileChannel.position(getPosition());
            }
        } catch (FileNotFoundException e) {
            logger.error("FileInterface not found in getFileChannel:", e);
            return null;
        } catch (IOException e) {
            logger.error("Change position in getFileChannel:", e);
            return null;
        }
        return fileChannel;
    }
