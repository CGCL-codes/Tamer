    public static void assumeReachable(URL url) {
        try {
            URLConnection c = url.openConnection();
            c.setConnectTimeout(2000);
            c.connect();
        } catch (Exception e) {
            Assume.assumeNoException(e);
        }
    }
