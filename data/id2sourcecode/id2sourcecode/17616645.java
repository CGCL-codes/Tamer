    public static void doTest(String suri) throws Exception {
        URL url = DataSetURI.getWebURL(DataSetURI.toUri(suri));
        URLConnection connect = url.openConnection();
        connect.setConnectTimeout(500);
        connect.connect();
    }
