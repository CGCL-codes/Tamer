    public static NetResponse ping(String urlAddress, int timeout) {
        URL url = null;
        URLConnection connection = null;
        NetResponse response = null;
        try {
            url = new URL(urlAddress);
            response = NetResponse.start(url, timeout);
            connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            response.stop(true);
            return response;
        } catch (Exception ex) {
            if (response != null) response.addException(ex); else {
                response = NetResponse.start(null, timeout);
                response.stop(false);
                response.addException(ex);
            }
        }
        return response;
    }
