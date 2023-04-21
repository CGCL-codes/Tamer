    private HttpsURLConnection connect(String login, String password) throws Exception {
        String body = "Email=" + login + "&Passwd=" + password + "&service=bookmarks";
        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        prepareConnection(body.length(), connection);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();
        return connection;
    }
