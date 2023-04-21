    public static ObjectInputStreamSource newFile(final File file) throws FileNotFoundException {
        checkNotNull(file);
        return new ObjectInputStreamSource(new RandomAccessFile(file, "r").getChannel());
    }
