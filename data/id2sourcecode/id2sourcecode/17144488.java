    protected FileChannel getFileChannelOut(File file, long position) throws ArFileException {
        FileOutputStream fileOutputStream = getFileOutputStream(file, position);
        FileChannel fileChannel = fileOutputStream.getChannel();
        if (position != 0) {
            try {
                fileChannel = fileChannel.position(position);
            } catch (IOException e) {
                throw new ArFileException("Change position in getFileChannelOut:", e);
            }
        }
        return fileChannel;
    }
