    protected static boolean isHostReachable(String hostName) {
        try {
            InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            String message = Logging.getMessage("NetworkStatus.UnreachableTestHost", hostName);
            Logging.logger().fine(message);
            return false;
        } catch (Exception e) {
            String message = Logging.getMessage("NetworkStatus.ExceptionTestingHost", hostName);
            Logging.logger().info(message);
            return false;
        }
        URLConnection connection = null;
        try {
            URL url = new URL("http://" + hostName);
            Proxy proxy = WWIO.configureProxy();
            if (proxy != null) connection = url.openConnection(proxy); else connection = url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            String ct = connection.getContentType();
            if (ct != null) return true;
        } catch (IOException e) {
            String message = Logging.getMessage("NetworkStatus.ExceptionTestingHost", hostName);
            Logging.logger().info(message);
        } finally {
            if (connection != null && connection instanceof HttpURLConnection) ((HttpURLConnection) connection).disconnect();
        }
        return false;
    }
