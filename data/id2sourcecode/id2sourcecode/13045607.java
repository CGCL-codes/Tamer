    private <T extends ScribdResponse> HttpURLConnection getConnection(ScribdMethod<T> method) throws IOException {
        HttpURLConnection connection;
        String urlParameters = method.getGETParametersForURL();
        URL url = new URL(this.getUrl() + "?" + urlParameters);
        System.out.println("call url " + url);
        System.out.println(url);
        assert this.getProxy() != null;
        connection = (HttpURLConnection) url.openConnection(this.getProxy());
        return connection;
    }
