    public static URLConnection openURL(URL url) throws IOException {
        for (int i = 0; i < LoadRetryTime; i++) {
            try {
                URLConnection c = url.openConnection();
                c.setConnectTimeout(LoadTimeOut);
                c.setReadTimeout(LoadTimeOut);
                c.connect();
                return c;
            } catch (Throwable e) {
                e.printStackTrace();
                System.err.println("openURL timeout, retry " + (i + 1) + "/" + LoadRetryTime);
            }
        }
        throw new IOException("openURL timeout : " + url.getPath());
    }
