    public static final UnSynchronizedBufferedWriter createBufferedWriter(final File file) throws FileNotFoundException {
        if (file == null) return null;
        if (file.isFile()) file.delete();
        final FileOutputStream outFile = new FileOutputStream(file);
        final FileChannel outChannel = outFile.getChannel();
        return new UnSynchronizedBufferedWriter(new OutputStreamWriter(Channels.newOutputStream(outChannel), Charset.forName(CHARSET)));
    }
