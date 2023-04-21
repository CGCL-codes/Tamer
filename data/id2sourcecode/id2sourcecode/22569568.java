    public static boolean isHttpServerReachable(String appid, boolean viaGoogle) {
        HttpURLConnection conn = null;
        try {
            String host = appid + ".appspot.com";
            String server = "http://" + host;
            URL url = new URL(server);
            Proxy proxy = null;
            if (viaGoogle) {
                proxy = new Proxy(Type.HTTP, new InetSocketAddress(GoogleAvailableService.getInstance().getAvailableHttpService(), 80));
                conn = (HttpURLConnection) (url.openConnection(proxy));
            } else if (GoogleAvailableService.getInstance().getMappingHost(host) != host) {
                proxy = new Proxy(Type.HTTP, new InetSocketAddress(GoogleAvailableService.getInstance().getMappingHost(host), 80));
                conn = (HttpURLConnection) (url.openConnection(proxy));
            } else {
                conn = (HttpURLConnection) (url.openConnection());
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        } finally {
            if (null != conn) {
                try {
                    conn.disconnect();
                } catch (Exception e2) {
                }
            }
        }
    }
