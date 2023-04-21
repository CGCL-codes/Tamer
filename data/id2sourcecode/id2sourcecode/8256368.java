    public static InputStream openResource(final URL url) throws IOException {
        if (null == url) throw new EOFException("openResource() no " + URL.class.getSimpleName() + " provided");
        try {
            if (isFileResource(url)) {
                final File f = toFile(url);
                if (null == f) throw new FileNotFoundException("openResource(" + url + ") no " + File.class.getSimpleName() + " converted");
                return new FileInputStream(f);
            }
            return url.openStream();
        } catch (RuntimeException e) {
            throw new StreamCorruptedException("openResource(" + url + ") " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
