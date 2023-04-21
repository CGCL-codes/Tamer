    public static final UnSynchronizedBufferedWriter createBufferedGZipWriter(final File file) throws IOException {
        if (file == null) return null;
        if (file.exists()) file.delete();
        final FileOutputStream outFile = new FileOutputStream(file);
        final FileChannel outChannel = outFile.getChannel();
        final GZIPOutputStream gzos = new GZIPOutputStream(Channels.newOutputStream(outChannel));
        return new UnSynchronizedBufferedWriter(new OutputStreamWriter(gzos, Charset.forName(CHARSET)));
    }
