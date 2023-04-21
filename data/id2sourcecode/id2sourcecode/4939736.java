    private static HttpURLConnection _getConnection(HttpPrincipal httpPrincipal) throws IOException {
        if (httpPrincipal == null || httpPrincipal.getUrl() == null) {
            return null;
        }
        URL url = null;
        if (httpPrincipal.getUserId() == null || httpPrincipal.getPassword() == null) {
            url = new URL(httpPrincipal.getUrl() + "/tunnel/servlet/TunnelServlet");
        } else {
            url = new URL(httpPrincipal.getUrl() + "/tunnel/servlet/AuthTunnelServlet");
        }
        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
        urlc.setDoInput(true);
        urlc.setDoOutput(true);
        urlc.setUseCaches(false);
        urlc.setRequestMethod("POST");
        if (httpPrincipal.getUserId() != null && httpPrincipal.getPassword() != null) {
            String userNameAndPassword = httpPrincipal.getUserId() + ":" + httpPrincipal.getPassword();
            urlc.setRequestProperty("Authorization", "Basic " + Base64.encode(userNameAndPassword.getBytes()));
        }
        return urlc;
    }
