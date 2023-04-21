    private static void testConnection() {
        URL url;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.valueOf(port)));
            url = new URL("http://www.yahoo.com");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
            String encoded = new String(Base64.encodeBase64(new String(username + ":" + password).getBytes()));
            uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
            uc.connect();
            if (uc.getResponseCode() == HttpURLConnection.HTTP_PROXY_AUTH) {
                requestAuthenticationCredentials();
            }
        } catch (MalformedURLException exception) {
            showConnectionErrorDialog(exception.getLocalizedMessage());
        } catch (IOException exception) {
            showConnectionErrorDialog(exception.getLocalizedMessage());
        }
    }
