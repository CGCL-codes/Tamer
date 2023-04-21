    public static InputStream readUrl(final URL url, final String username, final String password) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(url, username, password));
        if (null == url) {
            throw new RuntimeExceptionIsNull("url");
        }
        if (null == username) {
            throw new RuntimeExceptionIsNull("username");
        }
        if (!HelperString.isValid(username)) {
            throw new RuntimeExceptionIsEmpty("username");
        }
        if (null == password) {
            throw new RuntimeExceptionIsNull("password");
        }
        final URLConnection con = url.openConnection();
        con.setRequestProperty("Authorization", "Basic " + EncoderBase64.encode(username + ':' + password));
        con.setConnectTimeout(TIMEOUT);
        con.connect();
        final InputStream result = con.getInputStream();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
