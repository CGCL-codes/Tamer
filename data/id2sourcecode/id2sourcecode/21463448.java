    private String post(Map<String, String> formData, CalaisConfig config) throws IOException {
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> me : formData.entrySet()) {
            data.append(URLEncoder.encode(me.getKey(), "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(me.getValue(), "UTF-8"));
            data.append("&");
        }
        data.deleteCharAt(data.length() - 1);
        URL url = new URL(RESOURCE);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(config.get(ConnParam.CONNECT_TIMEOUT));
        conn.setReadTimeout(config.get(ConnParam.CONNECT_TIMEOUT));
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        try {
            out.write(data.toString());
            out.flush();
        } finally {
            out.close();
        }
        Reader in = new InputStreamReader(conn.getInputStream());
        try {
            return CharStreams.toString(in);
        } finally {
            in.close();
        }
    }
