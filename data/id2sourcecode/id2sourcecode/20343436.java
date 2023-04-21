    public static URLConnection openConnection(URL url, int timeout) throws IOException {
        return openConnection(url, timeout, null, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }
