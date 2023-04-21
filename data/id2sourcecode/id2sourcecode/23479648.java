    public static final int enumerateJAREntries(URL url, JarEntryHandler hndlr) throws IOException {
        if ((null == url) || (null == hndlr)) throw new IOException(ClassUtil.getExceptionLocation(JarUtils.class, "enumerateJAREntries") + " no URL/handler provided");
        final JarURLHandler urlh = (hndlr instanceof JarURLHandler) ? (JarURLHandler) hndlr : null;
        int nErr = (urlh != null) ? urlh.handleJarURL(url, true, 0) : 0;
        if (nErr != 0) return nErr;
        InputStream in = url.openStream();
        try {
            nErr = enumerateJAREntries(in, hndlr);
            if (urlh != null) nErr = urlh.handleJarURL(url, false, nErr);
            return nErr;
        } finally {
            FileUtil.closeAll(in);
        }
    }
