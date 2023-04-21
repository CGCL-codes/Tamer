    public static final ObjectOutputStream createObjectOutputWriter(final File file) throws IOException {
        if (file == null) return null;
        if (file.exists()) file.delete();
        final FileOutputStream outFile = new FileOutputStream(file);
        final FileChannel outChannel = outFile.getChannel();
        return new ObjectOutputStream(Channels.newOutputStream(outChannel));
    }
