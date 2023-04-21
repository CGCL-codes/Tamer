    private Response sendImpl(String from, String destNumber, String text) throws IOException {
        final QueryStringBuilder query = new QueryStringBuilder();
        query.append("username", apiKey);
        query.append("password", apiSecret);
        query.append("from", from);
        query.append("to", destNumber);
        query.append("type", "text");
        query.append("text", text);
        final URL url = new URL(NEXMO_GATEWAY_URL + query.toString());
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        final Reader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
        try {
            return mapper.readValue(reader, Response.class);
        } finally {
            reader.close();
        }
    }
