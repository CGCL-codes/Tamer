    private Document post(String callName, String callPrefix, String... param) throws IOException {
        URL url = new URL(requestUri + callPrefix + "/" + callName);
        HttpURLConnection handle = (HttpURLConnection) url.openConnection();
        handle.setDoOutput(true);
        StringBuilder data = new StringBuilder();
        data.append("apikey=").append(key).append("&outputMode=xml");
        for (int i = 0; i < param.length; ++i) {
            data.append('&').append(param[i]);
            if (++i < param.length) {
                data.append('=').append(URLEncoder.encode(param[i], "UTF8"));
            }
        }
        handle.addRequestProperty("Content-Length", Integer.toString(data.length()));
        DataOutputStream ostream = new DataOutputStream(handle.getOutputStream());
        ostream.write(data.toString().getBytes());
        ostream.close();
        return doRequest(handle);
    }
