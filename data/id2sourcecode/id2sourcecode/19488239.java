    public static final byte[] readBytesFromFile(final String filename) {
        try {
            final File file = new File(filename);
            final long fullsize = file.length();
            if (fullsize > Integer.MAX_VALUE) {
                throw new IOException("File too large");
            }
            final FileChannel channel = new FileInputStream(file).getChannel();
            final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            final byte[] result = new byte[(int) fullsize];
            buffer.get(result);
            return result;
        } catch (final Exception e) {
        }
        return null;
    }
