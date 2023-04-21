    private HttpURLConnection connectTo(String command, Map<String, String> arguments) throws IOException {
        StringBuffer urlB = new StringBuffer(getBaseUrl());
        urlB.append(command);
        if (arguments != null) {
            boolean first = true;
            for (Map.Entry<String, String> arg : arguments.entrySet()) {
                if (first) {
                    urlB.append("?");
                    first = false;
                } else {
                    urlB.append("&");
                }
                urlB.append(URLEncoder.encode(arg.getKey(), ENCODING_NAME)).append("=").append(URLEncoder.encode(arg.getValue(), ENCODING_NAME));
            }
        }
        URL url = new URL(urlB.toString());
        LogService.getRoot().info("Connecting to: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        authenticateIfLoggedIn(connection);
        return connection;
    }
