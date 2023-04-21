    public static void readFileAsStream(final File file, final OutputStream os) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, os));
        if (null == file) {
            throw new RuntimeExceptionIsNull("file");
        }
        if (null == os) {
            throw new RuntimeExceptionIsNull("os");
        }
        writeStream(os, readFile(file));
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
