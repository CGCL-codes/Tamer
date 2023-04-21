    private final Node openAuthConnection(String username, String password, String connection) throws JTweetException {
        try {
            URL url = new URL(connection);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String encode = Base64.encode(username + ":" + password);
            conn.addRequestProperty("Authorization", "Basic " + encode);
            conn.connect();
            BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
            if (builder == null) {
                builder = factory.newDocumentBuilder();
            }
            document = builder.parse(reader);
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            throw new JTweetException(e);
        }
        return document.getFirstChild();
    }
