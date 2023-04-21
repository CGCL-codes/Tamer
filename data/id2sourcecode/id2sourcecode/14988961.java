    public static boolean isUrl(String url) {
        try {
            java.net.URL l_url = new java.net.URL(url);
            java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
            if (System.getProperty("java.runtime.version").startsWith("1.4")) System.setProperty("sun.net.client.defaultReadTimeout", "2000"); else l_connection.setConnectTimeout(2000);
            l_connection.connect();
            l_connection.disconnect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
