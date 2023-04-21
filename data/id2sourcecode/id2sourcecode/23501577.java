    @Deprecated
    public static boolean doHttpPost(URL url, String charset, Map<String, String> params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            OutputStream out = conn.getOutputStream();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) out.write('&'); else first = false;
                out.write(URLEncoder.encode(entry.getKey(), charset).getBytes("US-ASCII"));
                out.write('=');
                out.write(URLEncoder.encode(entry.getValue(), charset).getBytes("US-ASCII"));
            }
            out.flush();
            out.close();
            conn.connect();
            int code = conn.getResponseCode();
            conn.disconnect();
            return code == HttpURLConnection.HTTP_OK;
        } catch (IOException ignored) {
            return false;
        }
    }
