    public static void aFile(URL url, File target) throws IOException {
        Check.notNull(url, "URL parameter may not be null!");
        Check.notNull(target, "Target file parameter may not be null!");
        InputStream stream = url.openStream();
        ReadableByteChannel rbc = Channels.newChannel(stream);
        FileOutputStream fos = new FileOutputStream(target);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        Close.quiet(fos.getChannel());
        Close.quiet(fos);
        Close.quiet(rbc);
        Close.quiet(stream);
    }
