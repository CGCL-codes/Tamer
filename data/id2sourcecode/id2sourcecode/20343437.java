    public static URLConnection openConnection(URL url, String clientCertAlias) throws IOException {
        return openConnection(url, 30000, clientCertAlias, SSLUtil.HOSTCERT_NORMAL_CHECK);
    }
