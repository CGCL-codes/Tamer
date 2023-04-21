    public static Response doHttpRequest(final String urlStr, final String requestMethod, final String body, final Map<String, String> header) throws Exception {
        HttpURLConnection conn;
        try {
            URL url = new URL(urlStr);
            if (proxyObj != null) {
                conn = (HttpURLConnection) url.openConnection(proxyObj);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(true);
            if (timeoutValue > 0) {
                LOG.debug("Setting connection timeout : " + timeoutValue);
                conn.setConnectTimeout(timeoutValue);
            }
            if (requestMethod != null) {
                conn.setRequestMethod(requestMethod);
            }
            if (header != null) {
                for (String key : header.keySet()) {
                    conn.setRequestProperty(key, header.get(key));
                }
            }
            OutputStreamWriter wr = null;
            if (body != null) {
                if (requestMethod != null && !MethodType.GET.toString().equals(requestMethod) && !MethodType.DELETE.toString().equals(requestMethod)) {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(body);
                    wr.flush();
                }
            }
            conn.connect();
        } catch (Exception e) {
            throw new SocialAuthException(e);
        }
        return new Response(conn);
    }
