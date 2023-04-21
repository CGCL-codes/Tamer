    public FileChannel getFileChannelIn(File file, long position) throws ArFileException {
        FileInputStream fileInputStream = getFileInputStream(file, 0);
        FileChannel fileChannel = fileInputStream.getChannel();
        if (position != 0) {
            try {
                fileChannel = fileChannel.position(position);
            } catch (IOException e) {
                throw new ArFileException("Change position in getFileChannelIn:", e);
            }
        }
        return fileChannel;
    }
