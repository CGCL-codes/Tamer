    public static final ObjectInputStream createObjectInputReader(final File file) throws IOException {
        if (file == null) return null;
        final FileInputStream inFile = new FileInputStream(file);
        final FileChannel inChannel = inFile.getChannel();
        return new ObjectInputStream(Channels.newInputStream(inChannel));
    }
