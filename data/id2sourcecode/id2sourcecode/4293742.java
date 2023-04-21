    public final HttpURLConnection setupConnection(String url, List<NameValuePair> params) throws MalformedURLException, IOException {
        if (params != null && !params.isEmpty()) {
            String param = "";
            for (NameValuePair pair : params) {
                param += "&" + pair.getEncodedPair();
            }
            param = param.substring(1);
            url += "?";
            url += param;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        if (userAgent != null) urlConnection.setRequestProperty("User-Agent", userAgent);
        return urlConnection;
    }
