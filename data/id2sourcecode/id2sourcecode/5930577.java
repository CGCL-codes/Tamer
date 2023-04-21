    public static InputStream readUrl(final URL url) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(url));
        if (null == url) {
            throw new RuntimeExceptionIsNull("url");
        }
        final URLConnection con = url.openConnection();
        con.setConnectTimeout(TIMEOUT);
        con.connect();
        final InputStream result = con.getInputStream();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
