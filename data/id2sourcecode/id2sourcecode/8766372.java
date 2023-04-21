    public static void writeFile(final File file, final InputStream is) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, is));
        writeFile(file, readStream(is), false);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
