    public static void copy(InputStream source, File destination, long size) throws IOException {
        if (source == null) throw new NullPointerException("The source may not be null.");
        if (destination == null) throw new NullPointerException("The destination may not be null.");
        if (size < 0) throw new IllegalArgumentException("The size may not be negative.");
        ReadableByteChannel sourceChannel = Channels.newChannel(source);
        try {
            destination.getParentFile().mkdirs();
            destination.getAbsoluteFile().getParentFile().mkdirs();
        } catch (Exception e) {
        }
        FileOutputStream destStream = new FileOutputStream(destination);
        try {
            FileChannel destChannel = destStream.getChannel();
            destChannel.transferFrom(sourceChannel, 0, size);
        } finally {
            try {
                source.close();
                destStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
