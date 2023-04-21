    public static final BufferedReader createBufferedReader(final File file) throws FileNotFoundException {
        if (file == null) return null;
        final FileInputStream inFile = new FileInputStream(file);
        final FileChannel inChannel = inFile.getChannel();
        return new BufferedReader(new InputStreamReader(Channels.newInputStream(inChannel)));
    }
