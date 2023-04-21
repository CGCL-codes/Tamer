    public static void writeFile(final File file, final InputStream is, final boolean append) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, is, append));
        writeFile(file, readStream(is), append);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
