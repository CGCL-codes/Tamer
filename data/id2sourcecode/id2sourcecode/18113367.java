    public static boolean isReachableURL(URL url) throws MalformedURLException, IOException, Exception {
        System.out.println("Testing to see if URL connects");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        System.out.println("Created HttpURLConnection object");
        conn.connect();
        System.out.println("connecting..");
        boolean isConnected = (conn.getContentLength() > 0);
        System.out.println("disconnecting..");
        conn.disconnect();
        System.out.println("disconnected");
        return isConnected;
    }
