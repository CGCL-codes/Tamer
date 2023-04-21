    public static boolean tryNetServer(String hostName, int serverPort, String path) throws MalformedURLException {
        log.debug("tryNetServer(hostName=" + hostName + ", serverPort=" + serverPort + ", path=" + path + ")");
        try {
            URL url = new URL("http://" + hostName + ":" + serverPort + "/");
            URLConnection uConn = url.openConnection();
            HttpURLConnection huConn = null;
            if (uConn instanceof HttpURLConnection) huConn = (HttpURLConnection) uConn;
            if (huConn.getResponseCode() == -1) return false;
        } catch (IOException e) {
            return false;
        }
        urlBase = new URL("http", hostName, serverPort, path);
        return true;
    }
